import React from 'react';
import styled from 'styled-components';

import ReservationCalendar from './ReservationCalendar';
import ReservationCapacityHandler from './ReservationCapacityHandler';
import ReservationBottomButtons from './ReservationBottomButtons';
import Modal from '../../utils/Modal';
import useReservation from './useReservation';

function ReservationAsideBar({ charge, slots }) {
  const {
    startDate,
    setStartDate,
    endDate,
    setEndDate,
    capacity,
    setCapacity,
    handleSubmit,
    modalOpen,
    IsPayment,
    getReservationInformation,
  } = useReservation(charge);

  const {
    chargePerHour,
    totalReservedTimeRange,
    totalCharges,
    chargeMultiplyTime,
  } = getReservationInformation();

  return (
    <form>
      <Container>
        <OuterWrapper display="flex" alignItems="flex-end">
          <div className="hour-charge">{chargePerHour}</div>
          <div className="hour">1시간</div>
        </OuterWrapper>
        <OuterWrapper>
          <div className="title">스케줄</div>
          <ReservationCalendar
            startDate={startDate}
            setStartDate={setStartDate}
            endDate={endDate}
            setEndDate={setEndDate}
            slots={slots}
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
              <div className="total-title">{totalReservedTimeRange}</div>
              <div className="total-detail">{chargeMultiplyTime}</div>
            </OuterWrapper>
            <OuterWrapper display="flex" justifyContent="space-between">
              <div className="total-title">합계</div>
              <div className="total-detail">{totalCharges}</div>
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
        {modalOpen && <Modal {...IsPayment} />}
        <ButtonsWrapper>
          <ReservationBottomButtons />
        </ButtonsWrapper>
      </Container>
    </form>
  );
}

const Container = styled.div`
  position: sticky;
  top: 195px;
  width: 18rem;
  height: flex;
  border-radius: 15px;
  box-shadow: rgba(0, 0, 0, 0.15) 0px 5px 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 30px 0px 30px;
  margin-bottom: 10px;
  background-color: #ffffff;

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
    color: black;
    font-size: 1rem;
    font-weight: 500;
    margin-bottom: 30px;
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
    width: 263px;
    height: 50px;
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #ffda77;
    border-radius: 10px;
    color: #2b2b2b;
    font: inherit;
    font-size: 1.1rem;
    font-weight: 500;
    border: none;
    padding: 3px 0px 0px 0px;

    :active {
      box-shadow: none;
      position: relative;
    }

    :disabled {
      cursor: not-allowed;
      background-color: #c9c9c9;
      opacity: 0.7;
    }
  }
`;

const OuterWrapper = styled.div`
  width: 100%;
  margin-top: 20px;
  padding-bottom: 30px;
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
