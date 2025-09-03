package finalmission.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    @DisplayName("도로명 주소를 생성한다.")
    void newAddress() {
        var address = new Address("군자로 123-1", "지하 1층");
        assertThat(address).isNotNull();
    }
}
