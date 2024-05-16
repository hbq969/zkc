pipeline {
    // Agent表示整个流水线或特定阶段中的步骤和命令执行的位置，
    // 该部分必须在 pipeline 块的顶层被定义，
    // 也可以在 stage 中再次定义，但是 stage 级别是可选的
    // 在任何可用的代理上执行流水线，配置语法
    agent any

    options{

        // 设置流水线的超时时间，超过流水线时间，job 会自动终止。
        // 如果不加 unit 参数默认为 1 分。
        timeout(time: 10, unit: 'HOURS')

        // 保留多少个流水线的构建记录
        buildDiscarder logRotator(artifactDaysToKeepStr: '2', artifactNumToKeepStr: '1', daysToKeepStr: '60', numToKeepStr: '20')

    }

    // 定义流水线的执行过程（相当于一个阶段）
    stages {

        stage('下载代码') {

            // 执行某阶段具体的步骤，注意凭证部分区分下是gerrit还gitlab
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '${BRANCH}']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'gerrit-code-clone', url: '${SCM_ADDRESS}']]])
            }

        }

        stage('sonar-scanner'){
            steps {
                script{
                    //与上面全局配置中的sonarqube scanner名称“sonar”要一致
                    sonarqubeScannerHome = tool 'sonar'
                }
                sh "${sonarqubeScannerHome}/bin/sonar-scanner  " +
                        "-Dsonar.host.url=http://100.76.10.200:9000 " +
                        "-Dsonar.projectKey=${SONAR_PROJECT_KEY} " + //sonar中项目的Key，约定填写仓库地址
                        "-Dsonar.projectName=${SCM_ADDRESS} " +  //sonar中项目名称，约定填写仓库地址
                        "-Dsonar.analysis.branch=${BRANCH} " +
                        "-Dsonar.projectVersion=1.0 " +
                        "-Dsonar.sources=${WORKSPACE} " +
                        "-Dsonar.java.binaries=${WORKSPACE} " +
                        "-Dsonar.analysis.buildNumber=${BUILD_SEQ} " +
                        "-Dsonar.login=587475932dab8d9ffbe50ef5b3dcdbe88e49fc51 " +
                        "-Dsonar.exclusions=${SONAR_EXCLUSIONS} "
            }
        }

        stage('单测扫描') {
            when {
                expression {
                    return true
                }
            }
            steps {
                //超时时间10分钟
                timeout(45) {
                    //执行单元测试和覆盖率检查
                    sh '''
                        cd ${WORKSPACE}
                        ls
                        #cd到pom所在目录执行mvn clover命令
                        mvn clean clover:setup test clover:aggregate clover:clover -Dmaven.test.failure.ignore -DnpmSkip=true -s /etc/maven/settings-nsad.xml
                        #clover.xml报告目录路径默认在target/site/clover下
                        clover_report_path=${WORKSPACE}/target/site
                        cd $clover_report_path
                        zip -rq clover.zip clover
                        cp clover/clover.xml unitTestResult-cicd.xml
                    '''
                    archiveArtifacts '**/target/**/unitTestResult*'

                    //发送扫描报告
                    step([$class: 'CloverPublisher', cloverReportDir: 'target/site/clover', cloverReportFileName: 'clover.xml'])

                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'target/site/clover', reportFiles: 'diff-cover-report.html', reportName: 'HTML Report', reportTitles: '本月增量单元测试覆盖率'])

                }
            }
        }

        stage('编译打包') {
            steps {
                sh '''
                mvn -DskipTests=true clean package
                '''
            }
        }

        stage('打镜像'){

            // ARTIFACT_NAME为代码仓库与制品配置中定义的制品名称
            // BUILD_SEQ为自动生成的，如果是集成在流水线中则格式为:版本号_序列号, 如果没有集成到流水线则格式为: 序列号
            steps{

                withCredentials([usernamePassword(credentialsId: 'docker-login-creds', passwordVariable: 'password', usernameVariable: 'username')]) {

                    sh '''
				    cd $WORKSPACE/target
                    fn=`ls -l *.tar.gz|awk '{print $NF}'`
                    tar -xvf ${fn}
                    appDir=${fn/.tar.gz/} && ls -l ${appDir}
                    cd ${appDir}/deploy/docker
                    cp Dockerfile ../../
                    docker build -t ${DOCKER_REPOSITORY}/${ARTIFACT_NAME}:${BUILD_SEQ} ../../
                    docker push ${DOCKER_REPOSITORY}/${ARTIFACT_NAME}:${BUILD_SEQ}
				    '''

                }

            }
        }

    }
}
