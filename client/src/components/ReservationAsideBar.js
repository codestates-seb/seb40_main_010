import React from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { useRecoilState } from 'recoil';
import {
  reservationStartDate,
  reservationEndDate,
  reservationMaxCapacity,
} from '../atoms';
import ReservationCalander from './ReservvationComponets/ReservationCalander';
import ReservationCapacityHandler from './ReservvationComponets/ReservationCapacityHandler';
import ReservationBottomButtons from './ReservvationComponets/ReservationBottomButtons';

// TODO : 시간이 콘솔로는 한국 표준시, 서버에 보낼 때는 UTC로 바뀌는 오류 발생
function ReservationAsideBar() {
  const [startDate, setStartDate] = useRecoilState(reservationStartDate);
  const [endDate, setEndDate] = useRecoilState(reservationEndDate);
  const [capacity, setCapacity] = useRecoilState(reservationMaxCapacity);

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

  const handleSubmit = () => {
    const article = {
      startTime: startDate,
      endTime: endDate,
      capacity,
    };
    axios.post('http://localhost:3001/reserve', article);
  };

  return (
    <form onSubmit={handleSubmit}>
      <Container>
        <OuterWrapper display="flex" alignItems="flex-end">
          <div className="hour-charge">{chargePerHourString}원</div>
          <div className="hour">1시간</div>
        </OuterWrapper>
        <OuterWrapper>
          <div className="title">스케줄</div>
          <ReservationCalander
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
          type="submit"
          className="reservation-button"
          disabled={!startDate || !endDate ? 'disabled' : null}
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
  margin-top: 90px;
  width: 18rem;
  height: flex;
  border-radius: 15px;
  border: 1px solid black;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0px 30px;

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
  display: ${porps => porps.display};
  justify-content: ${porps => porps.justifyContent};
  align-items: ${porps => porps.alignItems};
  margin-bottom: ${porps => porps.marginBottom};
`;

const ButtonsWrapper = styled.div`
  width: 100%;
  margin-top: 20px;
  padding-bottom: 40px;
`;

export default ReservationAsideBar;
