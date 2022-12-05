import { useState } from 'react';
import dayjs from 'dayjs';
import isBetween from 'dayjs/plugin/isBetween';
import { useRecoilState } from 'recoil';

import { reservationStartDateChangedState } from '../../atoms';

dayjs.extend(isBetween);

const useCalendar = ({ startDate, setStartDate, setEndDate, slots }) => {
  const [isStartDateSelected, setIsStartDateSelected] = useRecoilState(
    reservationStartDateChangedState,
  );
  const [startCalendarSelectedDay, setStartCalendarSelectedDay] =
    useState('dayNull');
  const [endCalendarSelectedDay, setEndCalendarSelectedDay] =
    useState('dayNull');

  const maxEndDate = new Date(startDate).setHours(23);

  const handleStartDate = time => {
    const clickedTime = new Date(time);
    const now = new Date();

    const isSameDate =
      startCalendarSelectedDay === 'dayNull' &&
      clickedTime.getDate() === now.getDate();

    const isToday = startCalendarSelectedDay === 'today';

    const isFuture = clickedTime.getDate() !== now.getDate();

    const isFromFuture =
      startCalendarSelectedDay === 'future' &&
      clickedTime.getDate() === now.getDate();

    if (isSameDate) {
      clickedTime.setHours(now.getHours() + 1);
      setStartDate(clickedTime);

      setStartCalendarSelectedDay('today');
    }

    if (isToday) {
      setStartDate(time);
    }

    if (isFuture) {
      setStartDate(time);

      setStartCalendarSelectedDay('future');
    }

    if (isFromFuture) {
      clickedTime.setHours(now.getHours() + 1);
      setStartDate(clickedTime);

      setStartCalendarSelectedDay('today');
    }

    setIsStartDateSelected(time);
    setEndDate(false);
    setEndCalendarSelectedDay('dayNull');
  };

  const handleEndDate = time => {
    const clickedTime = new Date(time);
    const startTime = new Date(startDate);

    if (clickedTime.getDate() === startTime.getDate()) {
      clickedTime.setHours(startTime.getHours() + 1);
      setEndDate(clickedTime);

      setEndCalendarSelectedDay('today');
    }
    if (endCalendarSelectedDay === 'today') {
      setEndDate(time);
    }
  };

  const getTimes = (slot, time) => {
    const targetTime = dayjs(time);
    const slotStartTime = dayjs(slot.startTime).add(9, 'hour');
    const slotEndTime = dayjs(slot.endTime).add(9, 'hour');
    const reservationStartTime = dayjs(startDate);
    return { targetTime, slotStartTime, slotEndTime, reservationStartTime };
  };

  const handleDisableStartTime = time => {
    const currentTime = new Date();
    const selectedTime = new Date(time);
    const isPastTime = currentTime.getTime() > selectedTime.getTime();

    if (isPastTime) return null;
    for (let i = 0; i < slots.length; i += 1) {
      const slot = slots[i];

      const { targetTime, slotStartTime, slotEndTime } = getTimes(slot, time);

      if (
        targetTime.isBetween(slotStartTime, slotEndTime) ||
        targetTime.isSame(dayjs(slotStartTime))
      ) {
        return false;
      }

      if (i + 1 === slots.length) {
        return true;
      }
    }
    return null;
  };

  const handleDisabledEndTime = time => {
    const selectedTime = new Date(time);
    const isStartTimePassed = new Date(startDate) < selectedTime.getTime();
    const isBeforeEndTime =
      selectedTime.getTime() <= new Date(maxEndDate).getTime();

    if (!(isStartTimePassed && isBeforeEndTime)) return null;
    for (let i = 0; i < slots.length; i += 1) {
      const slot = slots[i];

      const { targetTime, slotStartTime, slotEndTime, reservationStartTime } =
        getTimes(slot, time);

      if (
        targetTime.isBetween(slotStartTime, slotEndTime) ||
        targetTime.isSame(dayjs(slotEndTime))
      ) {
        return false;
      }

      if (
        reservationStartTime.isBefore(slotStartTime) &&
        targetTime.isAfter(slotEndTime)
      ) {
        return false;
      }

      if (i + 1 === slots.length) {
        return true;
      }
    }
    return null;
  };

  const handleDateChangeRaw = event => {
    event.preventDefault();
  };

  return {
    isStartDateSelected,
    handleStartDate,
    handleDisableStartTime,
    handleEndDate,
    maxEndDate,
    handleDisabledEndTime,
    handleDateChangeRaw,
  };
};

export default useCalendar;
