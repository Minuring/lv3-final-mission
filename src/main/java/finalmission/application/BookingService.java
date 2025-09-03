package finalmission.application;

import finalmission.domain.AuthenticationException;
import finalmission.domain.booking.Booking;
import finalmission.domain.booking.BookingDate;
import finalmission.domain.booking.BookingRepository;
import finalmission.domain.gym.Gym;
import finalmission.domain.gym.GymRepository;
import finalmission.domain.HolidayChecker;
import finalmission.domain.member.Member;
import finalmission.domain.member.MemberRepository;
import finalmission.exception.BusinessRuleException;
import finalmission.exception.DuplicatedException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final MemberRepository memberRepository;
    private final GymRepository gymRepository;
    private final BookingRepository bookingRepository;
    private final HolidayChecker holidayChecker;

    public void book(final String memberId, final UUID gymId, final LocalDate date) {
        checkNotHoliday(date);
        var bookingDate = BookingDate.ofNew(date);
        var member = memberRepository.getById(memberId);
        var gym = gymRepository.getById(gymId);
        checkDuplicates(member, gym, bookingDate);
        bookingRepository.save(new Booking(member, gym, bookingDate));
    }

    public List<Booking> getMyBookings(final String memberId) {
        var member = memberRepository.getById(memberId);
        return bookingRepository.findAllByMember(member);
    }

    @Transactional
    public Booking modifyDate(final UUID id, final String memberId, final LocalDate dateToModify) {
        checkNotHoliday(dateToModify);
        var member = memberRepository.getById(memberId);
        var booking = bookingRepository.getById(id);
        if (booking.ownedBy(member)) {
            var toModify = BookingDate.ofNew(dateToModify);
            checkDuplicates(member, booking.getGym(), toModify);
            booking.modifyDate(toModify);
            return booking;
        }
        throw new AuthenticationException("수정하려는 사용자가 예약자와 일치하지 않습니다. 현재 사용자 ID : " + memberId + ", 예약자 ID : " + booking.getMember().getId());
    }

    public void cancel(final UUID id, final String memberId) {
        var member = memberRepository.getById(memberId);
        var booking = bookingRepository.getById(id);
        if (booking.ownedBy(member)) {
            bookingRepository.delete(booking);
            return;
        }
        throw new AuthenticationException("취소하려는 사용자가 예약자와 일치하지 않습니다. 현재 사용자 ID : " + memberId + ", 예약자 ID : " + booking.getMember().getId());
    }

    private void checkNotHoliday(final LocalDate date) {
        if (holidayChecker.isHoliday(date)) {
            throw new BusinessRuleException("공휴일에는 예약할 수 없습니다.");
        }
    }

    private void checkDuplicates(final Member member, final Gym gym, final BookingDate date) {
        var duplicated = bookingRepository.existsByMemberAndGymAndDate(member, gym, date);
        if (duplicated) {
            throw new DuplicatedException("이미 예약되었습니다.");
        }
    }
}
