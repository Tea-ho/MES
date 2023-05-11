import React, {useState} from 'react';

import Box from '@mui/material/Box';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';

// 컴포넌트 가져오기
import CreateProduct from './CreateProduct';
import PlanProduct from './PlanProduct';
import ManageProduct from './ManageProduct';

export default function ProductTab(){ /*제품 부분의 화면을 바꿔줄 탭바*/
  const [value, setValue] = useState(0);
  const [screen, setScreen] = useState(<CreateProduct/>); //화면을 바꿔주는

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
    //바뀔때마다 아래 화면을 바꿔주는 화면
    if(newValue == 0){ //제품 생산
        setScreen(<CreateProduct/>)
    }else if(newValue == 1){//제품 관리
        setScreen(<ManageProduct/>)
    }else if(newValue == 2){ //제품 지시
        setScreen(<PlanProduct/>)
    }
  };

    return(<>
        <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
          <Tabs value={value} onChange={handleChange} centered>
            <Tab label="제품 생산" />
            <Tab label="제품 관리" />
            <Tab label="제품 지시" />
          </Tabs>
          {screen}
        </Box>
    </>)
}