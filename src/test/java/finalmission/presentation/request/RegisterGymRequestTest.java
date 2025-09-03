package finalmission.presentation.request;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import finalmission.domain.member.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterGymRequestTest {

    @Test
    @DisplayName("원시값으로부터 주소를 만든다.")
    void address() {
        var request = new RegisterGymRequest("짐박스", "도로명", "상세");

        var address = request.address();

        assertAll(
            () -> assertThat(address).isInstanceOf(Address.class),
            () -> assertThat(address.street()).isEqualTo("도로명"),
            () -> assertThat(address.detail()).isEqualTo("상세")
        );
    }
}
