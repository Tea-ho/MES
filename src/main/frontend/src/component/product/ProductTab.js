import React, {useState} from 'react';

import Box from '@mui/material/Box';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';

export default function ProductTab(){ /*제품 부분의 화면을 바꿔줄 탭바*/
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
    console.log(newValue);
    if(newValue == 1){

    }
  };

    return(<>
        <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
          <Tabs value={value} onChange={handleChange} centered>
            <Tab label="제품 생산" />
            <Tab label="제품 지시" />
            <Tab label="제품 공정" />
          </Tabs>
        </Box>
    </>)
}