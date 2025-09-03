package finalmission.infra;

import finalmission.domain.HolidayChecker;
import finalmission.infra.HolidayCheckerClientConfig.HolidayResponse;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayCheckerClient implements HolidayChecker {

    private @Value("${api.get-holiday.service-key}") String serviceKey;
    private @Value("${api.get-holiday.base-url}") String baseUrl;
    private final RestTemplate restTemplate;

    @Override
    public boolean isHoliday(final LocalDate date) {
        var uri = createGetHolidayUri(date);
        try {
            var response = restTemplate.getForObject(uri, HolidayResponse.class);
            return isHolidayForDate(date, response, uri);
        } catch (RestClientResponseException e) {
            log.error("공휴일 API 요청에 실패했습니다 : ", e);
            throw e;
        }
    }

    private boolean isHolidayForDate(final LocalDate date, final HolidayResponse response, final URI uri) {
        return Optional.ofNullable(response)
            .map(r -> r.isHoliday(date))
            .orElseThrow(() -> {
                log.error("공휴일 API 응답이 null입니다. 요청한 URI = {}", uri);
                return new RuntimeException("공휴일 API에서 비정상적인 응답을 받았습니다.");
            });
    }

    private URI createGetHolidayUri(final LocalDate date) {
        return UriComponentsBuilder
            .fromUriString(baseUrl)
            .queryParam("_type", "json")
            .queryParam("solYear", date.getYear())
            .queryParam("solMonth", String.format("%02d", date.getMonthValue()))
            .queryParam("ServiceKey", serviceKey)
            .build()
            .toUri();
    }
}
