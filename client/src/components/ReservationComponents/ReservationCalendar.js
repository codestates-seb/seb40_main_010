import React, { useState } from 'react';
import dayjs from 'dayjs';
import isBetween from 'dayjs/plugin/isBetween';
import { useRecoilState } from 'recoil';
import styled from 'styled-components';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';

import 'react-datepicker/dist/react-datepicker.css';

import { reservationStartDateChangedState } from '../../atoms';

dayjs.extend(isBetween);

export default function ReservationCalendar({
  startDate,
  setStartDate,
  endDate,
  setEndDate,
}) {
  const [isStartDateSelected, setIsStartDateSelected] = useRecoilState(
    reservationStartDateChangedState,
  );
  const [startCalendarSelectedDay, setStartCalendarSelectedDay] =
    useState('dayNull');
  const [endCalendarSelectedDay, setEndCalendarSelectedDay] =
    useState('dayNull');

  const maxEndDate = new Date(startDate).setDate(
    new Date(startDate).getDate() + 1,
  );

  const handleStartDate = time => {
    const clickedTime = new Date(time);
    const now = new Date();

    if (
      startCalendarSelectedDay === 'dayNull' &&
      clickedTime.getDate() === now.getDate()
    ) {
      clickedTime.setHours(now.getHours() + 1);
      setStartDate(clickedTime);

      setStartCalendarSelectedDay('today');
    }

    if (startCalendarSelectedDay === 'today') {
      setStartDate(time);
    }

    if (clickedTime.getDate() !== now.getDate()) {
      setStartDate(time);

      setStartCalendarSelectedDay('future');
    }

    if (
      startCalendarSelectedDay === 'future' &&
      clickedTime.getDate() === now.getDate()
    ) {
      clickedTime.setHours(now.getHours() + 1);
      setStartDate(clickedTime);

      setStartCalendarSelectedDay('today');
    }

    setIsStartDateSelected(time);
    setEndDate(false);
  };

  const handleEndDate = time => {
    const clickedTime = new Date(time);
    const startTime = new Date(startDate);

    if (
      endCalendarSelectedDay === 'dayNull' &&
      clickedTime.getDate() === startTime.getDate()
    ) {
      clickedTime.setHours(startTime.getHours() + 1);
      setEndDate(clickedTime);

      setEndCalendarSelectedDay('today');
    }

    if (
      endCalendarSelectedDay === 'dayNull' &&
      clickedTime.getDate() !== startTime.getDate()
    ) {
      setEndDate(time);

      setEndCalendarSelectedDay('nextDay');
    }

    if (endCalendarSelectedDay === 'today') {
      setEndDate(time);
    }

    if (
      clickedTime.getDate() !== startTime.getDate() &&
      clickedTime.getHours() > startTime.getHours()
    ) {
      clickedTime.setHours(startTime.getHours());
      setEndDate(clickedTime);

      setEndCalendarSelectedDay('nextDay');
    }

    if (
      endCalendarSelectedDay === 'nextDay' &&
      clickedTime.getDate() === startTime.getDate()
    ) {
      clickedTime.setHours(startTime.getHours() + 1);
      setEndDate(clickedTime);

      setEndCalendarSelectedDay('today');
    }

    if (
      endCalendarSelectedDay === 'nextDay' &&
      clickedTime.getDate() !== startTime.getDate()
    ) {
      setEndDate(clickedTime);
    }
  };

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

        const x = dayjs(time);
        const startTime = dayjs(slot.start);
        const endTime = dayjs(slot.end);

        if (x.isBetween(startTime, endTime) || x.isSame(dayjs(startTime))) {
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

        const x = dayjs(time);
        const y = dayjs(startDate);
        const slotStartTime = dayjs(slot.start);
        const slotEndTime = dayjs(slot.end);

        if (
          x.isBetween(slotStartTime, slotEndTime) ||
          x.isSame(dayjs(slotEndTime))
        ) {
          return false;
        }

        if (y.isBefore(slotStartTime) && x.isAfter(slotEndTime)) {
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
