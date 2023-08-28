import React, { useState } from 'react';
import axios from 'axios';


/**
*------------------------------------------------------------------------
* 2023.08.28 유재호
*
* naver API를 이용한 블로그 검색//미완
*------------------------------------------------------------------------
*/
const BlogSearchComponent: React.FC = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<any[]>([]);

  const handleSearch = async () => {
    try {
      const clientId = 'oD3ecS2HMpot9lYmZ0Fw';
      const clientSecret = 'SNPjZxlf**';

      const response = await axios.get(
        `https://openapi.naver.com/v1/search/blog?query=${encodeURIComponent(query)}`,
        {
          headers: {
            'X-Naver-Client-Id': clientId,
            'X-Naver-Client-Secret': clientSecret,
          },
        }
      );

      setResults(response.data.items);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  return (
    <div>
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Enter a search query"
      />
      <button onClick={handleSearch}>Search</button>

      <ul>
        {results.map((result, index) => (
          <li key={index}>{result.title}</li>
        ))}
      </ul>
    </div>
  );
};

export default BlogSearchComponent;