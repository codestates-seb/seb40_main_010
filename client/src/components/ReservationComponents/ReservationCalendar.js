import React from 'react';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import setHours from 'date-fns/setHours';
import setMinutes from 'date-fns/setMinutes';
import { ko } from 'date-fns/esm/locale';

import 'react-datepicker/dist/react-datepicker.css';

import useCalendar from './useCalendar';

function ReservationCalendar({
  startDate,
  setStartDate,
  endDate,
  setEndDate,
  slots,
}) {
  const {
    isStartDateSelected,
    handleStartDate,
    handleDisableStartTime,
    handleEndDate,
    maxEndDate,
    handleDisabledEndTime,
    handleDateChangeRaw,
  } = useCalendar({ startDate, setStartDate, endDate, setEndDate, slots });

  return (
    <Container>
      <DatePick marginBottom="25px">
        <Label>시작일시</Label>
        <DatePicker
          selectsStart
          showTimeSelect
          selected={startDate}
          onChange={handleStartDate}
          startDate={startDate}
          endDate={endDate}
          minDate={new Date()}
          excludeTimes={[setHours(setMinutes(new Date(), 0), 23)]}
          locale={ko}
          dateFormat="yyyy년 MM월 dd일 a hh시"
          timeIntervals={60}
          placeholderText="스케줄을 선택하세요"
          filterTime={handleDisableStartTime}
          disabledKeyboardNavigation
          required
          onChangeRaw={handleDateChangeRaw}
        />
      </DatePick>
      <DatePick marginTop="10px">
        <Label>종료일시</Label>
        <DatePicker
          selectsEnd
          showTimeSelect
          selected={endDate}
          onChange={handleEndDate}
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
          onChangeRaw={handleDateChangeRaw}
        />
      </DatePick>
    </Container>
  );
}

const DatePick = styled.div`
  margin-bottom: ${({ marginBottom }) => marginBottom};
  margin-top: ${({ marginTop }) => marginTop};
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
    font-family: 'GmarketSans';
    font-weight: 600;
  }
  .react-datepicker__navigation-icon::before {
    border-color: white;
  }

  .react-datepicker__day {
    color: #2b2b2b;
    font-family: 'GmarketSans';
    font-size: 0.71rem;
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
    color: white;
  }

  li.react-datepicker__time-list-item {
    font-family: 'GmarketSans';
    font-size: 0.71rem;
  }

  .react-datepicker__day--disabled {
    color: #aeaeae;
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
    font: inherit;
    color: #2b2b2b;
    font-size: 0.8rem;
    :focus-visible {
      outline: #96c2ff auto 1px;
    }
  }
`;

export default ReservationCalendar;
