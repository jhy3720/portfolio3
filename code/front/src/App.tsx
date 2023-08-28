// App.tsx

import './App.css';
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import ReactDOM from 'react-dom';
import Main from './main/Main';
import Registercomponent from './components/Register';

/**
*------------------------------------------------------------------------
* 2023.07.05 유재호
* 
* 라우터를 이용하여 페이지 교체를 위해 App에서 Main으로 코드 이동
* 페이지 변경시 사용되는 메소드
*------------------------------------------------------------------------
*/
function App() {

  return (
    <div className="App">

      {/*페이지 교체를 위한 라우터 */}
      <BrowserRouter>
       
          <Routes>
            
            <Route path="/register" element={<Registercomponent />} />
            <Route path="/" element={<Main />} />

          </Routes>

      </BrowserRouter> 

    </div>
  );
}

export default App;