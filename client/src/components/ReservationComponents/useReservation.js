import axios from 'axios';
import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import {
  reservationStartDate,
  reservationEndDate,
  reservationMaxCapacity,
} from '../../atoms';

import { onClickPaymentButton } from '../../utils/payment';

const useReservation = charge => {
  const [startDate, setStartDate] = useRecoilState(reservationStartDate);
  const [endDate, setEndDate] = useRecoilState(reservationEndDate);
  const [capacity, setCapacity] = useRecoilState(reservationMaxCapacity);
  const [modalOpen, setModalOpen] = useState(false);
  const [reserveId, setReserveId] = useState(null);

  const { id } = useParams();

  const navigator = useNavigate();

  const getReservationInformation = () => {
    const timeDiff =
      new Date(endDate).getTime() - new Date(startDate).getTime();
    const reservedTimeRange = timeDiff / (1000 * 60 * 60);

    const chargePerHourString = new Intl.NumberFormat('ko-KR').format(charge);

    const totalCharge = reservedTimeRange * charge;
    const totalChargeString = new Intl.NumberFormat('ko-KR').format(
      totalCharge,
    );

    const chargePerHour = `${chargePerHourString}원`;
    const totalReservedTimeRange = `총 ${reservedTimeRange}시간`;
    const totalCharges = `총 ${totalChargeString}원`;
    const chargeMultiplyTime = `총 ${chargePerHourString}원 x ${reservedTimeRange}시간`;
    return {
      chargePerHour,
      totalReservedTimeRange,
      totalCharges,
      chargeMultiplyTime,
    };
  };

  const onClickPaymentKaKaoButton = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/place/reserve/${reserveId}/payment`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      const paymentUrl = response.data.data;
      onClickPaymentButton(paymentUrl);
      setModalOpen(false);
    } catch (error) {
      console.log('Error', error);
    }
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
      alert('로그인이 필요합니다.');
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
        `${process.env.REACT_APP_SERVER_BASE_URL}/place/${id}/reserve`,
        reservationInformation,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );

      setReserveId(response.data);

      await axios.post(
        `${process.env.REACT_APP_SERVER_BASE_URL}/reserve/${response.data}/mail`,
        {},
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      setStartDate(false);
      setEndDate(false);
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  return {
    startDate,
    setStartDate,
    endDate,
    setEndDate,
    capacity,
    setCapacity,
    handleSubmit,
    modalOpen,
    IsPayment,
    onClickPaymentButton,
    getReservationInformation,
  };
};

export default useReservation;
