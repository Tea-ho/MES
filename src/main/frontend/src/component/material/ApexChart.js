import React, { useEffect } from "react";
import Chart from "react-apexcharts";

const ApexChart = (props) => {
  useEffect(() => {
    console.log(props.categories);
    console.log(props.data);
  }, [props]);

  const options = {
    chart: {
      id: "basic-bar"
    },
    xaxis: {
      categories: props.categories
    }

  };

  const series = [
    {
      name: "총재고량",
      data: props.data
    }
  ];

  return (
    <div className="ApexChart">
      <div className="row">
        <div className="mixed-chart">
          <Chart options={options} series={series} type="line" width="100%" height="400"/>
        </div>
      </div>
    </div>
  );
};

export default ApexChart;