import React from 'react';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { ko } from 'date-fns/esm/locale';
import { useRecoilState } from 'recoil';
import { reservationStartDateChangedState } from '../../atoms';

function ReservationCalander({ startDate, setStartDate, endDate, setEndDate }) {
  const [isStartDateSelected, setIsStartDateSelected] = useRecoilState(
    reservationStartDateChangedState,
  );

  const handleStartDate = time => {
    setIsStartDateSelected(time);
    setStartDate(time);
    setEndDate(false);
  };

  const filterStartTime = time => {
    const selectedDate = new Date(time);
    return new Date(startDate).getTime() < selectedDate.getTime();
  };

  const filterPassedTime = time => {
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
          locale={ko}
          dateFormat="yyyy년 MM월 dd일 a hh시"
          timeIntervals={60}
          placeholderText="스케줄을 선택하세요"
          filterTime={filterPassedTime}
          className="calander"
          disabledKeyboardNavigation
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
          locale={ko}
          dateFormat="yyyy년 MM월 dd일 a hh시"
          timeIntervals={60}
          placeholderText="스케줄을 선택하세요"
          filterTime={filterStartTime}
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
  }
`;
