// import React from 'react';
// import styled from 'styled-components';

export const onClickPaymentButton = link => {
  window.open(link, '_blank');
};

// export default function Payment(link) {
//   const onClickPaymentButton = () => {
//     window.open(link, '_blank');
//   };

//   return (
//     <BlurBackground>
//       <PayModalContainer>
//         <PaymentButton onClick={onClickPaymentButton}>결제하기</PaymentButton>
//       </PayModalContainer>
//     </BlurBackground>
//   );
// }

// const PayModalContainer = styled.div`
//   box-sizing: border-box;
//   display: flex;
//   justify-content: space-around;
//   align-items: center;
//   flex-direction: column;
//   width: 430px;
//   height: 240px;
//   box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
//   border-radius: 20px;
//   padding: 20px;
//   background-color: #ffffff;

//   z-index: 999;
//   position: absolute;
//   top: 50%;
//   left: 50%;
//   transform: translate(-50%, -50%);
// `;

// const BlurBackground = styled.div`
//   display: flex;
//   align-items: center;
//   justify-content: center;
//   background-color: rgba(255, 255, 255, 0.8);
//   z-index: 100;
//   position: absolute;
//   top: 50%;
//   left: 50%;
//   transform: translate(-50%, -50%);
//   width: 100%;
//   height: 100%;
// `;

// const PaymentButton = styled.button`
//   box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
//   background-color: #e0e0e0;
//   color: #303030;
//   width: 140px;
//   height: 45px;
//   border: none;
//   font-size: 25px;
//   border-radius: 30px;
//   font-weight: bold;
//   :hover {
//     background-color: #eb7470;
//     color: #ffffff;
//     cursor: pointer;
//   }
// `;
