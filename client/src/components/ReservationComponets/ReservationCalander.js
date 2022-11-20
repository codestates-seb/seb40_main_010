import React from 'react';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { ko } from 'date-fns/esm/locale';
// import setHours from 'date-fns/setHours';
// import setMinutes from 'date-fns/setMinutes';
import subDays from 'date-fns/subDays';
// import addDays from 'date-fns/addDays';
import { useRecoilState } from 'recoil';
import { reservationStartDateChangedState } from '../../atoms';

// TODO:
// 날짜를 선택하면 시간이 자동으로 오전 12시로 선택되는 오류
// 예약된 시간 어떻게 막을 건지
function ReservationCalander({ startDate, setStartDate, endDate, setEndDate }) {
  const [isStartDateSelected, setIsStartDateSelected] = useRecoilState(
    reservationStartDateChangedState,
  );

  const maxEndDate = new Date(startDate).setDate(
    new Date(startDate).getDate() + 1,
  );

  // TODO: 예약된 시간 막기 테스트용 더미 데이터
  // const events = [
  //   {
  //     start: '2022-11-26T02:00:00.000Z',
  //     end: '2022-11-26T05:00:00.000Z',
  //   },
  // ];

  // const disabledDateRanges = events.map(range => ({
  //   start: new Date(range.start),
  //   end: new Date(range.end),
  // }));

  console.log(subDays(new Date(), 5));
  console.log(new Date('2022-11-26T02:00:00.000Z'));

  const handleStartDate = time => {
    setIsStartDateSelected(!isStartDateSelected);
    setStartDate(time);
    setEndDate(false);
  };

  const handleDisabledEndTime = time => {
    const selectedDate = new Date(time);
    return (
      new Date(startDate).getTime() < selectedDate.getTime() &&
      selectedDate.getTime() <= new Date(maxEndDate).getTime()
    );
  };

  const handleFilterPassedTime = time => {
    const currentDate = new Date();
    const selectedDate = new Date(time);
    return currentDate.getTime() < selectedDate.getTime();
  };

  return (
    <Container>
      <DatePick marginBottom="8px">
        <Label>시작일시</Label>
        <DatePicker
          selectsStart
          showTimeSelect
          selected={startDate}
          onChange={handleStartDate}
          startDate={startDate}
          endDate={endDate}
          minDate={new Date()}
          // minTime={data => console.log(data)}
          locale={ko}
          dateFormat="yyyy년 MM월 dd일 a hh시"
          timeIntervals={60}
          placeholderText="스케줄을 선택하세요"
          filterTime={handleFilterPassedTime}
          className="calander"
          disabledKeyboardNavigation
          // TODO: 예약된 시간 막기
          // excludeDateIntervals={[
          //   { start: subDays(new Date(), 5), end: addDays(new Date(), 5) },
          // ]}
          excludeDateIntervals={[
            {
              start: new Date('2022-11-26T02:00:00.000Z'),
              end: new Date('2022-11-26T05:00:00.000Z'),
            },
          ]}
        />
      </DatePick>
      <DatePick>
        <Label>종료일시</Label>
        <DatePicker
          selectsEnd
          showTimeSelect
          selected={endDate}
          onChange={date => setEndDate(date)}
          startDate={startDate}
          endDate={endDate}
          minDate={startDate}
          maxDate={maxEndDate}
          locale={ko}
          dateFormat="yyyy년 MM월 dd일 a hh시"
          timeIntervals={60}
          placeholderText="스케줄을 선택하세요"
          filterTime={handleDisabledEndTime}
          disabled={!isStartDateSelected}
          disabledKeyboardNavigation
        />
      </DatePick>
    </Container>
  );
}

export default ReservationCalander;

const DatePick = styled.div`
  margin-bottom: ${porps => porps.marginBottom};
`;

const Label = styled.div`
  width: 5rem;
  color: #2b2b2b;
  font-size: 0.8rem;
  font-weight: 500;
`;

const Container = styled.div`
  .react-datepicker__header {
    background-color: #eb7470;
    border-bottom: none;
  }

  .react-datepicker__current-month,
  .react-datepicker-time__header,
  .react-datepicker__day-name {
    color: white;
  }
  .react-datepicker__navigation-icon::before {
    border-color: white;
  }

  .react-datepicker__day--in-selecting-range,
  .react-datepicker__day--in-range,
  .react-datepicker__day--selected,
  .react-datepicker__time-container
    .react-datepicker__time
    .react-datepicker__time-box
    ul.react-datepicker__time-list
    li.react-datepicker__time-list-item--selected {
    background-color: #ffda77;
  }

  .react-datepicker__time-list::-webkit-scrollbar {
    width: 10px;
  }
  .react-datepicker__time-list::-webkit-scrollbar-thumb {
    background-color: #96c2ff;
  }

  input {
    width: 97%;
    margin-top: 10px;
    height: 2rem;
    padding: 0px 0px 0px 8px;
    :focus-visible {
      outline: #96c2ff auto 1px;
    }
  }
`;
