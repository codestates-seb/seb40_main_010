import React from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import styled from 'styled-components';
import axios from 'axios';

import {
  reservationStartDate,
  reservationEndDate,
  reservationMaxCapacity,
  PlaceIDState,
} from '../../atoms';

import ReservationCalendar from './ReservationCalendar';
import ReservationCapacityHandler from './ReservationCapacityHandler';
import ReservationBottomButtons from './ReservationBottomButtons';

// TODO
// 23~32까지 파일로 빼기
function ReservationAsideBar() {
  const [startDate, setStartDate] = useRecoilState(reservationStartDate);
  const [endDate, setEndDate] = useRecoilState(reservationEndDate);
  const [capacity, setCapacity] = useRecoilState(reservationMaxCapacity);
  const placeId = useRecoilValue(PlaceIDState);

  const timeDiff = new Date(endDate).getTime() - new Date(startDate).getTime();
  const reservedTimeRange = timeDiff / (1000 * 60 * 60);
  const chargePerHour = 79000;
  const chargePerHourString = chargePerHour
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  const totalCharge = reservedTimeRange * chargePerHour;
  const totalChargeString = totalCharge
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');

  const handleSubmit = async event => {
    event.preventDefault();

    const reservationInformation = {
      startTime: startDate,
      endTime: endDate,
      capacity,
    };
    try {
      const response = await axios.post(
        `/place/${placeId}/reserve`,
        reservationInformation,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
            RefreshToken: localStorage.getItem('REFRESH'),
          },
        },
      );
      console.log(response);
      setStartDate(false);
      setEndDate(false);
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  return (
    <form>
      <Container>
        <OuterWrapper display="flex" alignItems="flex-end">
          <div className="hour-charge">{chargePerHourString}원</div>
          <div className="hour">1시간</div>
        </OuterWrapper>
        <OuterWrapper>
          <div className="title">스케줄</div>
          <ReservationCalendar
            startDate={startDate}
            setStartDate={setStartDate}
            endDate={endDate}
            setEndDate={setEndDate}
          />
        </OuterWrapper>
        <OuterWrapper>
          <div className="title">총인원</div>
          <ReservationCapacityHandler
            capacity={capacity}
            setCapacity={setCapacity}
          />
        </OuterWrapper>
        {endDate ? (
          <>
            <OuterWrapper display="flex" justifyContent="space-between">
              <div className="total-title">총 {reservedTimeRange}시간</div>
              <div className="total-detail">
                {chargePerHourString}원 x {reservedTimeRange}시간
              </div>
            </OuterWrapper>
            <OuterWrapper display="flex" justifyContent="space-between">
              <div className="total-title">합계</div>
              <div className="total-detail">{totalChargeString}원</div>
            </OuterWrapper>
          </>
        ) : null}
        <button
          onClick={handleSubmit}
          type="submit"
          className="reservation-button"
          disabled={!(startDate && endDate)}
        >
          예약하기
        </button>
        <ButtonsWrapper>
          <ReservationBottomButtons />
        </ButtonsWrapper>
      </Container>
    </form>
  );
}

const Container = styled.div`
  position: sticky;
  top: 140px;
  width: 18rem;
  height: flex;
  border-radius: 15px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0px 30px;
  margin-bottom: 10px;

  .hour-charge {
    display: flex;
    color: #2b2b2b;
    font-size: 1.1rem;
    font-weight: 600;
  }

  .hour {
    color: #2b2b2b;
    font-size: 0.85rem;
    margin-left: 5px;
  }

  .title {
    color: #2b2b2b;
    font-size: 1rem;
    font-weight: 600;
    margin-bottom: 15px;
  }

  .total-title {
    color: #2b2b2b;
    font-size: 1rem;
    font-weight: 500;
    border-bottom: 1px solid #2b2b2b;
  }

  .total-detail {
    color: #2b2b2b;
    font-size: 1rem;
    font-weight: 500;
  }

  .reservation-button {
    width: 120px;
    height: 50px;
    margin-top: 20px;
    background-color: #ffda77;
    border-radius: 15px;
    color: #2b2b2b;
    font-size: 1.1rem;
    font-weight: 600;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;

    :active {
      box-shadow: none;
      position: relative;
      box-shadow: rgba(0, 0, 0, 0.35) 1px 1px 1px;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      :active {
        box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;
      }
    }
  }
`;

const OuterWrapper = styled.div`
  width: 100%;
  margin-top: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e7eaee;
  display: ${({ display }) => display};
  justify-content: ${({ justifyContent }) => justifyContent};
  align-items: ${({ alignItems }) => alignItems};
  margin-bottom: ${({ marginBottom }) => marginBottom};
`;

const ButtonsWrapper = styled.div`
  width: 100%;
  margin-top: 20px;
  padding-bottom: 40px;
`;

export default ReservationAsideBar;
