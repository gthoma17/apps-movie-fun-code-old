package org.superbiz.moviefun;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class Application {
    @Value("${s3.endpointUrl}") String s3EndpointUrl;
    @Value("${s3.accessKey}") String s3AccessKey;
    @Value("${s3.secretKey}") String s3SecretKey;
    @Value("${s3.bucketName}") String s3BucketName;

    @Bean
    public org.superbiz.moviefun.BlobStore blobStore() {
        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        s3Client.setEndpoint(s3EndpointUrl);

        s3Client.setEndpoint(s3EndpointUrl);

        return new S3Store(s3Client, s3BucketName);
    }


    @Bean
    public ServletRegistrationBean actionServletRegistration(ActionServlet actionServlet) {
        return new ServletRegistrationBean(actionServlet, "/moviefun/*");
    }
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
