package com.catcher.batch.infrastructure.utils;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.AWSKMSException;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import com.catcher.batch.common.ErrorCode;
import com.catcher.batch.common.exception.ExternalException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class KmsUtils {

    @Value("${aws.kms.keyId}")
    private static String KEY_ID;

//    @Value("${spring.profiles.active}")
//    private static String PROFILE;

    public static String encrypt(String text) {
        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

            EncryptRequest request = new EncryptRequest();
            request.withKeyId(KEY_ID);
            request.withPlaintext(ByteBuffer.wrap(text.getBytes(StandardCharsets.UTF_8)));
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT);

            byte[] cipherBytes = kmsClient.encrypt(request).getCiphertextBlob().array();
            return Base64.encodeBase64String(cipherBytes);
        } catch (AWSKMSException exception) {
            throw new ExternalException(ErrorCode.AWS_KMS_INVALID_ERROR.getStatus(), exception.getMessage());
        }
    }

    public static String decrypt(String cipherBase64) {
        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

            DecryptRequest request = new DecryptRequest();
            request.withKeyId(KEY_ID);
            request.withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(cipherBase64)));
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT);

            byte[] textBytes = kmsClient.decrypt(request).getPlaintext().array();
            return new String(textBytes);
        } catch (AWSKMSException exception) {
            throw new ExternalException(ErrorCode.AWS_KMS_INVALID_ERROR.getStatus(), exception.getMessage());
        }
    }
}