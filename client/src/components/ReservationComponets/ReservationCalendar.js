import React from 'react';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { ko } from 'date-fns/esm/locale';
import moment from 'moment';
import { useRecoilState } from 'recoil';
import { reservationStartDateChangedState } from '../../atoms';

function ReservationCalendar({ startDate, setStartDate, endDate, setEndDate }) {
  const [isStartDateSelected, setIsStartDateSelected] = useRecoilState(
    reservationStartDateChangedState,
  );

  const maxEndDate = new Date(startDate).setDate(
    new Date(startDate).getDate() + 1,
  );

  const handleStartDate = time => {
    setIsStartDateSelected(time);
    setStartDate(time);
    setEndDate(false);
  };

  // TODO: 예약된 시간 비활성화 테스트용 더미 데이터
  const slots = [
    {
      start: '2022-11-27T09:00:00.000Z',
      end: '2022-11-27T11:00:00.000Z',
    },
  ];

  // eslint-disable-next-line consistent-return
  const handleDisableStartTime = time => {
    const currentTime = new Date();
    const selectedTime = new Date(time);
    const isPastTime = currentTime.getTime() > selectedTime.getTime();
    if (!isPastTime) {
      for (let i = 0; i < slots.length; i += 1) {
        const slot = slots[i];

        const x = moment(time);
        const startTime = moment(slot.start);
        const endTime = moment(slot.end);

        if (
          x.isBetween(startTime, endTime) ||
          x.isSame(moment(startTime)) ||
          x.isSame(moment(endTime))
        ) {
          return false;
        }
        if (i + 1 === slots.length) {
          return true;
        }
      }
    }
  };

  // eslint-disable-next-line consistent-return
  const handleDisabledEndTime = time => {
    const selectedTime = new Date(time);
    const isStartTimePassed = new Date(startDate) < selectedTime.getTime();
    const isBeforeEndTime =
      selectedTime.getTime() <= new Date(maxEndDate).getTime();

    if (isStartTimePassed && isBeforeEndTime) {
      for (let i = 0; i < slots.length; i += 1) {
        const slot = slots[i];

        const x = moment(time);
        const startTime = moment(slot.start);
        const endTime = moment(slot.end);

        if (
          x.isBetween(startTime, endTime) ||
          x.isSame(moment(startTime)) ||
          x.isSame(moment(endTime))
        ) {
          return false;
        }
        if (i + 1 === slots.length) {
          return true;
        }
      }
    }
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
          filterTime={handleDisableStartTime}
          disabledKeyboardNavigation
          required
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

export default ReservationCalendar;

const DatePick = styled.div`
  margin-bottom: ${props => props.marginBottom};
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
