import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import header from '../../utils/header';

import {
  reservationStartDate,
  reservationEndDate,
  reservationMaxCapacity,
  PlaceIDState,
} from '../../atoms';

const useReservation = charge => {
  const [startDate, setStartDate] = useRecoilState(reservationStartDate);
  const [endDate, setEndDate] = useRecoilState(reservationEndDate);
  const [capacity, setCapacity] = useRecoilState(reservationMaxCapacity);
  const placeId = useRecoilValue(PlaceIDState);
  const [modalOpen, setModalOpen] = useState(false);

  const navigator = useNavigate();

  const timeDiff = new Date(endDate).getTime() - new Date(startDate).getTime();
  const reservedTimeRange = timeDiff / (1000 * 60 * 60);

  const chargePerHour = charge;
  const chargePerHourString = new Intl.NumberFormat('ko-KR', {
    currency: 'KRW',
    // style: 'currency',
    currencyDisplay: 'code',
  }).format(charge);

  const totalCharge = reservedTimeRange * chargePerHour;
  const totalChargeString = new Intl.NumberFormat('ko-KR').format(totalCharge);

  const onClickPaymentKaKaoButton = async () => {
    // const response = await axios.get(`/place/reserve/?/payment`);
    // const paymentUrl = response.data.data;
    // onClickPaymentButton(paymentUrl);
    setModalOpen(false);
  };

  const IsPayment = {
    modalText: '결제하시겠습니까?',
    modalActionText: '결제하기',
    modalAction: onClickPaymentKaKaoButton,
    modalOpen,
    setModalOpen,
  };

  // eslint-disable-next-line consistent-return
  const handleSubmit = async event => {
    event.preventDefault();
    const isLogIn = localStorage.getItem('ACCESS');
    if (!isLogIn) {
      setStartDate(false);
      setEndDate(false);
      setCapacity(1);
      navigator('/log-in');
    }

    setModalOpen(true);

    const reservationInformation = {
      startTime: startDate,
      endTime: endDate,
      capacity,
    };

    try {
      const response = await axios.post(
        `/place/${placeId}/reserve`,
        // JSON.stringify(reservationInformation),
        reservationInformation,
        header,
      );
      console.log(response.data);
      setStartDate(false);
      setEndDate(false);
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  return {
    chargePerHourString,
    startDate,
    setStartDate,
    endDate,
    setEndDate,
    capacity,
    setCapacity,
    handleSubmit,
    modalOpen,
    IsPayment,
    // onClickPaymentButton,
    reservedTimeRange,
    totalChargeString,
  };
};

export default useReservation;
