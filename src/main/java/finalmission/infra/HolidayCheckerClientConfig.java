package finalmission.infra;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HolidayCheckerClientConfig {

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
            .defaultHeader("content-type", "application/json;charset=UTF-8")
            .defaultHeader("Accept", "application/json;q=0.9")
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30));
    }

    public record HolidayResponse(Response response) {

        public boolean isHoliday(final LocalDate date) {
            return response.body().items().item().stream()
                .anyMatch(item -> item.sameDateWith(date));
        }

        private record Response(
            Header header,
            Body body
        ) {

            private record Header(
                String resultCode,
                String resultMsg
            ) {

            }

            private record Body(
                Items items,
                int numOfRows,
                int pageNo,
                int totalCount
            ) {

                private record Items(
                    List<Item> item
                ) {

                    private record Item(
                        String dateKind,
                        String dateName,
                        String isHoliday,
                        String locdate,
                        int seq
                    ) {

                        public boolean sameDateWith(final LocalDate date) {
                            var itemDate = LocalDate.parse(locdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                            return itemDate.equals(date);
                        }
                    }
                }
            }
        }
    }
}
