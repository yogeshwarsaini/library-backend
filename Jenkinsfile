pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        AWS_REGION            = 'ap-south-1'
        EB_APP_NAME           = 'library-app'
        EB_ENV_NAME           = 'Library-app-env'
        S3_BUCKET             = 'library-frontend-yogesh'
        CF_DISTRIBUTION_ID    = 'E3MSSZS1M0NVSO'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Code checkout...'
                checkout scm
            }
        }

        stage('Backend Build') {
            steps {
                echo '🏗️ Maven build...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy Backend to Beanstalk') {
            steps {
                echo '🚀 Deploying to Elastic Beanstalk...'
                sh """
                    # JAR ko S3 pe upload karo
                    aws s3 cp target/library-backend-0.0.1-SNAPSHOT.jar \
                        s3://${S3_BUCKET}/backend/library-backend.jar \
                        --region ${AWS_REGION}

                    # Beanstalk application version banao
                    aws elasticbeanstalk create-application-version \
                        --application-name ${EB_APP_NAME} \
                        --version-label v-\$(date +%Y%m%d%H%M%S) \
                        --source-bundle S3Bucket=${S3_BUCKET},S3Key=backend/library-backend.jar \
                        --region ${AWS_REGION}

                    # Environment update karo
                    aws elasticbeanstalk update-environment \
                        --environment-name ${EB_ENV_NAME} \
                        --version-label v-\$(date +%Y%m%d%H%M%S) \
                        --region ${AWS_REGION}
                """
            }
        }

        stage('Health Check - Backend') {
            steps {
                echo '✔️ Backend health check...'
                sh """
                    sleep 60
                    curl -f http://${EB_ENV_NAME}.eba-3ptkthcj.ap-south-1.elasticbeanstalk.com/actuator/health || exit 1
                """
            }
        }
    }

    post {
        success { echo '✅ Backend deployed!' }
        failure { echo '❌ Pipeline failed!' }
    }
}
