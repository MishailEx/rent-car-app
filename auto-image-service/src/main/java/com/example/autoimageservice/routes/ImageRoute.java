package com.example.autoimageservice.routes;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class ImageRoute {

    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Autowired
    public ImageRoute(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(POST("/add"), this::uploadFile);
    }


    // в случае privet bucket
//    private Mono<ServerResponse> getImages(ServerRequest serverRequest) {
//        int expiry = 60;
//        GetPresignedObjectUrlArgs urlArgs = new GetPresignedObjectUrlArgs.Builder()
//                .method(Method.GET)
//                .bucket(bucketName)
//                .object()
//                .expiry(expiry)
//                .build();
//        try {
//            String presignedUrl = minioClient.getPresignedObjectUrl(urlArgs);
//        }
//    }


    public Mono<String> getStringFromMultipartData(Part part) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return DataBufferUtils.write(part.content(), outputStream)
                .then(Mono.defer(() -> {
                    byte[] data = outputStream.toByteArray();
                    String result = new String(data, StandardCharsets.UTF_8);
                    return Mono.just(result);
                }));
    }

    public Mono<ServerResponse> uploadFile(ServerRequest request) {

        Mono<MultiValueMap<String, Part>> multiValueMapMono = request.multipartData();
        return multiValueMapMono.flatMap(file -> {
            List<Part> parts = file.get("images[]");

            return getStringFromMultipartData(file.get("id").get(0)).flatMap(id -> {
                List<String> urls = new ArrayList<>();
                for (Part part : parts) {
                    String randomUUID = UUID.randomUUID().toString();
                    String fileName = id + " - " + randomUUID + ".jpg";
                    urls.add(fileName);


                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    DataBufferUtils.write(part.content(), outputStream)
                            .doOnComplete(() -> {
                                byte[] data = outputStream.toByteArray();

                                try {
                                    minioClient.putObject(PutObjectArgs.builder()
                                            .bucket(bucketName)
                                            .object(fileName)
                                            .stream(new ByteArrayInputStream(data), data.length, -1)
                                            .contentType("image/jpeg")
                                            .build());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }).subscribe();
                }
                HashMap<String, List<String>> resp = new HashMap<>();
                resp.put("urls", urls);
                return ServerResponse.ok().bodyValue(resp);
            });
        });
    }
}


