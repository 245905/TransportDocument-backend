package com.tui.transport;

import com.tui.transport.services.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class MfaServiceTest {

    @Autowired
    private SmsService smsService;

    @Test
    void shouldSendAndVerifyCode() {
        Map<String, Object> sendResult = smsService.sendCode("48881413105");

        String id = sendResult.get("id").toString();
        String code = sendResult.get("code").toString();

        System.out.println("📋 ID: " + id);
        System.out.println("🔐 Kod: " + code);

        assertThat(id).isNotNull();
        assertThat(code).isNotNull();


        Boolean verifyResult = smsService.verifyCode("48881413105", code);

        System.out.println("✅ Weryfikacja: " + verifyResult);
        assertThat(verifyResult).isTrue();
    }
}
