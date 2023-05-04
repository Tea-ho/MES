import React, {useState, useEffect} from 'react';
import axios from 'axios'

export default function CreateProduct(){
    let[pageInfo, setPageInfo] = useState({"page" : 1, "key" : '', "keyword " : ''}) //검색기능과 페이지네이션을 위해

    return( <>제품 생산</>);
}