package com.emir.mytasks.mytasks_backend.service;

import com.emir.mytasks.mytasks_backend.dto.MotivationQuoteResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MotivationService {

    private static final String ZEN_QUOTES_URL = "https://zenquotes.io/api/random";

    // Küçük proje için basit RestTemplate yeterli
    private final RestTemplate restTemplate = new RestTemplate();

    public MotivationQuoteResponse getDailyQuote() {
        try {
            ZenQuoteApiResponse[] response =
                    restTemplate.getForObject(ZEN_QUOTES_URL, ZenQuoteApiResponse[].class);

            if (response != null && response.length > 0 && response[0] != null) {
                ZenQuoteApiResponse z = response[0];
                return new MotivationQuoteResponse(z.getQ(), z.getA());
            }
        } catch (Exception ex) {
            // İstersen burada logger ile loglayabilirsin
            // log.warn("ZenQuotes isteği başarısız", ex);
        }

        // Dış servis patlarsa gösterilecek yedek söz
        return new MotivationQuoteResponse(
                "Bugün attığın küçük bir adım bile ilerlemedir.",
                "MyTasks"
        );
    }

    /**
     * ZenQuotes JSON'uyla bire bir eşleşen iç sınıf:
     * [
     *   { "q": "...", "a": "..." , ... }
     * ]
     */
    private static class ZenQuoteApiResponse {
        private String q;
        private String a;

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }
}
