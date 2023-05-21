import React, { useEffect, useRef } from 'react';
import Chart from 'chart.js/auto';

const SalesChart = ({ list }) => {
  const chartRef = useRef(null);
  const chartInstance = useRef(null);

  useEffect(() => {
    if (chartInstance.current) {
      // 기존의 차트 객체가 존재하는 경우 제거
      chartInstance.current.destroy();
    }

    if (chartRef.current) {
      const ctx = chartRef.current.getContext('2d');

      // 데이터 가공
      const filteredList = list.filter(item => item.order_status === 2);

      const processedData = {};
      filteredList.forEach(item => {
        const { prodId, prodName, salesPrice, orderCount } = item;
        if (processedData[prodId]) {
          // 이미 해당 prodId의 데이터가 존재하는 경우
          processedData[prodId].salesPriceSum += salesPrice;
          processedData[prodId].salesPriceCount += 1;
          processedData[prodId].orderCount += orderCount;
        } else {
          // 해당 prodId의 데이터가 처음 등장하는 경우
          processedData[prodId] = {
            prodName,
            salesPriceSum: salesPrice,
            salesPriceCount: 1,
            orderCount,
          };
        }
      });

      const data = {
        labels: Object.values(processedData).map(item => item.prodName),
        datasets: [
          {
            label: '판매개수(백 개)',
            data: Object.values(processedData).map(item => item.orderCount / 100) ,
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
          },
          {
            label: '판매가격(천 원)',
            data: Object.values(processedData).map(item => {
              const avgSalesPrice = item.salesPriceSum / item.salesPriceCount / 1000;
              return avgSalesPrice;
            }),
            backgroundColor: 'rgba(254, 128, 162, 1)',
            borderColor: 'rgba(254, 72, 121, 1)',
            borderWidth: 1,
          },
          // 다른 데이터셋 추가 가능
        ],
      };

      // 차트 생성
      chartInstance.current = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
            },
          },
        },
      });
    }
  }, [list]);

  return (
    <div>
      <canvas ref={chartRef} width={400} height={400} />
    </div>
  );
};

export default SalesChart;