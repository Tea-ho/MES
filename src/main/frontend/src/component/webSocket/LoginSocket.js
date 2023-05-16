import React,{ useState , useEffect , useRef} from 'react';
import axios from 'axios';
import { BrowserRouter , Routes , Route } from "react-router-dom";




export default function LoginSocket(props) {
    console.log('LoginSocket실행중')

    let [ id , setId ] = useState(''); // 익명채팅에서 사용할 id [ 난수 저장 ]
    let [msg , setMsg ] = useState([]);

    let ws = useRef( null ) ;   // 1.모든 함수 사용할 클라이언트소켓 변수

    const [code , setCode ] = useState();



    useEffect(() => {

    if( !ws.current ){      // 3.클라이언트소켓이 접속이 안되어있을때. [ * 유효성검사 ]

                ws.current = new WebSocket("ws://localhost:8080/chat") ; // 4. 서버소켓 연결

                ws.current.onopen = () => {   console.log('서버 접속했습니다.');}

                ws.current.onmessage = (e) => {  console.log('서버소켓으로 메시지 받음');  console.log( e);  console.log( e.data );
                    let data = JSON.parse( e.data );
                    let member = JSON.parse(sessionStorage.getItem('member'));
                    if(data == 0 && member.position == "임원"){
                    alert('결제확인이 필요합니다.')
                    }
                    if(data == 1 && member.position != "임원"){
                    alert('결제되었습니다.')
                    }
                    setMsg( (msg)=> [...msg , data  ] ); // 재 렌더링
                }
            }
    } , [])



  return (<>

  </>);
}
