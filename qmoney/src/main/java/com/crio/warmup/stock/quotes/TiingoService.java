
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  public static final String TOKEN = "78aaa0b09dbdfcd8988593f5c4a322b13479a28a";
  private RestTemplate restTemplate;

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  // ./gradlew test --tests TiingoServiceTest


  // CHECKSTYLE:OFF

  // Write a method to create appropriate url to call the Tiingo API.
  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {
    List<Candle> stocksStartToEndDate;

    if (from.compareTo(to) >= 0) {
      throw new RuntimeException();
    }

    String url = buildUri(symbol, from, to);

    String stocks = restTemplate.getForObject(url, String.class);

    ObjectMapper objectMapper = getObjectMapper();

    TiingoCandle[] stocksStartToEndDateArray = objectMapper.readValue(stocks, TiingoCandle[].class);

    if (stocksStartToEndDateArray != null) {
      stocksStartToEndDate = Arrays.asList(stocksStartToEndDateArray);
    } else {
      throw new StockQuoteServiceException("INVALID RESPONSE FOUND");
    }

    return stocksStartToEndDate;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = String.format(
        "https://api.tiingo.com/tiingo/daily/%s/prices?" + "startDate=%s&endDate=%s&token=%s",
        symbol, startDate, endDate, TOKEN);
    return uriTemplate;
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }



  // 1. Update the method signature to match the signature change in the interface.
  // Start throwing new StockQuoteServiceException when you get some invalid response from
  // Tiingo, or if Tiingo returns empty results for whatever reason, or you encounter
  // a runtime exception during Json parsing.
  // 2. Make sure that the exception propagates all the way from
  // PortfolioManager#calculateAnnualisedReturns so that the external user's of our API
  // are able to explicitly handle this exception upfront.

  // CHECKSTYLE:OFF


}
