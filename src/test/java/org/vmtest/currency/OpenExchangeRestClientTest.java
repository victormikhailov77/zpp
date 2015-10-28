package org.vmtest.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.vmtest.TestSuiteConfiguration;
import org.vmtest.currency.model.CurrencyRates;
import org.vmtest.currency.service.CurrencyService;
import org.vmtest.currency.service.openexchangerates.OpenExchangeRates;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by victor on 23.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestSuiteConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class OpenExchangeRestClientTest {

        private static final String openExchangeUrl = "https://openexchangerates.org/api/latest.json?app_id=14b7fdd1aed0454c9fd7ff6b31a389b1&base=USD";

        private static final String openExchangeHistoricalUrl = "https://openexchangerates.org/api/api/historical/2015-10-19.json?app_id=14b7fdd1aed0454c9fd7ff6b31a389b1&base=USD";

        private static final String openExchangeExpectedJson = "{\n" +
                "  \"disclaimer\": \"Exchange rates are provided for informational purposes only, and do not constitute financial advice of any kind. Although every attempt is made to ensure quality, NO guarantees are given whatsoever of accuracy, validity, availability, or fitness for any purpose - please use at your own risk. All usage is subject to your acceptance of the Terms and Conditions of Service, available at: https://openexchangerates.org/terms/\",\n" +
                "  \"license\": \"Data sourced from various providers with public-facing APIs; copyright may apply; resale is prohibited; no warranties given of any kind. Bitcoin data provided by http://coindesk.com. All usage is subject to your acceptance of the License Agreement available at: https://openexchangerates.org/license/\",\n" +
                "  \"timestamp\": 1445565609,\n" +
                "  \"base\": \"USD\",\n" +
                "  \"rates\": {\n" +
                "    \"AED\": 3.672878,\n" +
                "    \"AFN\": 64.545,\n" +
                "    \"ALL\": 125.3779,\n" +
                "    \"AMD\": 471.5975,\n" +
                "    \"ANG\": 1.7887,\n" +
                "    \"AOA\": 135.280331,\n" +
                "    \"ARS\": 9.508681,\n" +
                "    \"AUD\": 1.3819,\n" +
                "    \"AWG\": 1.793333,\n" +
                "    \"AZN\": 1.046663,\n" +
                "    \"BAM\": 1.756548,\n" +
                "    \"BBD\": 2,\n" +
                "    \"BDT\": 78.84641,\n" +
                "    \"BGN\": 1.757055,\n" +
                "    \"BHD\": 0.377163,\n" +
                "    \"BIF\": 1560.8475,\n" +
                "    \"BMD\": 1,\n" +
                "    \"BND\": 1.397655,\n" +
                "    \"BOB\": 6.965006,\n" +
                "    \"BRL\": 3.911938,\n" +
                "    \"BSD\": 1,\n" +
                "    \"BTC\": 0.0036321649,\n" +
                "    \"BTN\": 65.120384,\n" +
                "    \"BWP\": 10.558788,\n" +
                "    \"BYR\": 17259.7,\n" +
                "    \"BZD\": 2.005766,\n" +
                "    \"CAD\": 1.309045,\n" +
                "    \"CDF\": 928.223,\n" +
                "    \"CHF\": 0.971543,\n" +
                "    \"CLF\": 0.024602,\n" +
                "    \"CLP\": 689.980803,\n" +
                "    \"CNY\": 6.35755,\n" +
                "    \"COP\": 2922.621657,\n" +
                "    \"CRC\": 535.567298,\n" +
                "    \"CUC\": 1,\n" +
                "    \"CUP\": 1.005163,\n" +
                "    \"CVE\": 99.31237755,\n" +
                "    \"CZK\": 24.31757,\n" +
                "    \"DJF\": 177.640002,\n" +
                "    \"DKK\": 6.69933,\n" +
                "    \"DOP\": 45.3049,\n" +
                "    \"DZD\": 105.5632,\n" +
                "    \"EEK\": 13.989825,\n" +
                "    \"EGP\": 8.030821,\n" +
                "    \"ERN\": 15.14,\n" +
                "    \"ETB\": 21.12846,\n" +
                "    \"EUR\": 0.90067,\n" +
                "    \"FJD\": 2.1237,\n" +
                "    \"FKP\": 0.649166,\n" +
                "    \"GBP\": 0.649166,\n" +
                "    \"GEL\": 2.390875,\n" +
                "    \"GGP\": 0.649166,\n" +
                "    \"GHS\": 3.822396,\n" +
                "    \"GIP\": 0.649166,\n" +
                "    \"GMD\": 38.96385,\n" +
                "    \"GNF\": 7327.737598,\n" +
                "    \"GTQ\": 7.707387,\n" +
                "    \"GYD\": 206.447336,\n" +
                "    \"HKD\": 7.750053,\n" +
                "    \"HNL\": 22.20926,\n" +
                "    \"HRK\": 6.843705,\n" +
                "    \"HTG\": 54.00865,\n" +
                "    \"HUF\": 278.832901,\n" +
                "    \"IDR\": 13525.416667,\n" +
                "    \"ILS\": 3.876645,\n" +
                "    \"IMP\": 0.649166,\n" +
                "    \"INR\": 64.85655,\n" +
                "    \"IQD\": 1175.2075,\n" +
                "    \"IRR\": 29964,\n" +
                "    \"ISK\": 127.297999,\n" +
                "    \"JEP\": 0.649166,\n" +
                "    \"JMD\": 119.965,\n" +
                "    \"JOD\": 0.708496,\n" +
                "    \"JPY\": 120.5747,\n" +
                "    \"KES\": 102.1341,\n" +
                "    \"KGS\": 68.848299,\n" +
                "    \"KHR\": 4059.692476,\n" +
                "    \"KMF\": 438.892932,\n" +
                "    \"KPW\": 899.91,\n" +
                "    \"KRW\": 1130.500007,\n" +
                "    \"KWD\": 0.301821,\n" +
                "    \"KYD\": 0.825342,\n" +
                "    \"KZT\": 281.593688,\n" +
                "    \"LAK\": 8137.965098,\n" +
                "    \"LBP\": 1508.681667,\n" +
                "    \"LKR\": 142.631499,\n" +
                "    \"LRD\": 84.66847,\n" +
                "    \"LSL\": 13.421275,\n" +
                "    \"LTL\": 3.04089,\n" +
                "    \"LVL\": 0.625769,\n" +
                "    \"LYD\": 1.360431,\n" +
                "    \"MAD\": 9.755241,\n" +
                "    \"MDL\": 20.1324,\n" +
                "    \"MGA\": 3053.243333,\n" +
                "    \"MKD\": 54.24086,\n" +
                "    \"MMK\": 1080.8919,\n" +
                "    \"MNT\": 1991.166667,\n" +
                "    \"MOP\": 8.0258,\n" +
                "    \"MRO\": 299.047402,\n" +
                "    \"MTL\": 0.683738,\n" +
                "    \"MUR\": 35.52045,\n" +
                "    \"MVR\": 15.315333,\n" +
                "    \"MWK\": 550.114919,\n" +
                "    \"MXN\": 16.5029,\n" +
                "    \"MYR\": 4.232459,\n" +
                "    \"MZN\": 43.2,\n" +
                "    \"NAD\": 13.50212,\n" +
                "    \"NGN\": 199.1361,\n" +
                "    \"NIO\": 27.57816,\n" +
                "    \"NOK\": 8.273842,\n" +
                "    \"NPR\": 104.8304,\n" +
                "    \"NZD\": 1.463626,\n" +
                "    \"OMR\": 0.385029,\n" +
                "    \"PAB\": 1,\n" +
                "    \"PEN\": 3.26935,\n" +
                "    \"PGK\": 2.93685,\n" +
                "    \"PHP\": 46.46398,\n" +
                "    \"PKR\": 104.414601,\n" +
                "    \"PLN\": 3.823886,\n" +
                "    \"PYG\": 5663.035,\n" +
                "    \"QAR\": 3.640172,\n" +
                "    \"RON\": 3.975153,\n" +
                "    \"RSD\": 107.88324,\n" +
                "    \"RUB\": 62.58907,\n" +
                "    \"RWF\": 742.384495,\n" +
                "    \"SAR\": 3.749895,\n" +
                "    \"SBD\": 7.909238,\n" +
                "    \"SCR\": 12.751013,\n" +
                "    \"SDG\": 6.122674,\n" +
                "    \"SEK\": 8.431257,\n" +
                "    \"SGD\": 1.388524,\n" +
                "    \"SHP\": 0.649166,\n" +
                "    \"SLL\": 3745.3745,\n" +
                "    \"SOS\": 639.984753,\n" +
                "    \"SRD\": 3.2875,\n" +
                "    \"STD\": 21847.6,\n" +
                "    \"SVC\": 8.806446,\n" +
                "    \"SYP\": 188.777998,\n" +
                "    \"SZL\": 13.42002,\n" +
                "    \"THB\": 35.46226,\n" +
                "    \"TJS\": 6.575,\n" +
                "    \"TMT\": 3.501367,\n" +
                "    \"TND\": 1.982099,\n" +
                "    \"TOP\": 2.22778,\n" +
                "    \"TRY\": 2.870243,\n" +
                "    \"TTD\": 6.360878,\n" +
                "    \"TWD\": 32.41051,\n" +
                "    \"TZS\": 2207.22665,\n" +
                "    \"UAH\": 22.44603,\n" +
                "    \"UGX\": 3616.053333,\n" +
                "    \"USD\": 1,\n" +
                "    \"UYU\": 29.69821,\n" +
                "    \"UZS\": 2654.224976,\n" +
                "    \"VEF\": 6.320658,\n" +
                "    \"VND\": 22285.65,\n" +
                "    \"VUV\": 111.331251,\n" +
                "    \"WST\": 2.546593,\n" +
                "    \"XAF\": 588.98269,\n" +
                "    \"XAG\": 0.0630395,\n" +
                "    \"XAU\": 0.000858,\n" +
                "    \"XCD\": 2.70102,\n" +
                "    \"XDR\": 0.711358,\n" +
                "    \"XOF\": 589.70031,\n" +
                "    \"XPD\": 0.00146,\n" +
                "    \"XPF\": 107.024487,\n" +
                "    \"XPT\": 0.000988,\n" +
                "    \"YER\": 214.9848,\n" +
                "    \"ZAR\": 13.41054,\n" +
                "    \"ZMK\": 5252.024745,\n" +
                "    \"ZMW\": 12.137888,\n" +
                "    \"ZWL\": 322.387247\n" +
                "  }\n" +
                "}";


        @Autowired
        @Qualifier("openExchangeRestClient")
        CurrencyService service;

        private RestTemplate template = mock(RestTemplate.class);

        private final String source = "USD";
        private final String[] currencies = new String[]{"GBP", "EUR", "CHF", "SEK", "NOK", "PLN", "CZK", "DKK"};

        @Before
        public void initialize() throws IOException {
                ReflectionTestUtils.setField(service, "restTemplate", template);

                ObjectMapper mapper = new ObjectMapper();
                OpenExchangeRates openExchangeExpectedRates = mapper.readValue(openExchangeExpectedJson, OpenExchangeRates.class);

                when(template.getForObject(openExchangeUrl, OpenExchangeRates.class)).thenReturn(openExchangeExpectedRates);
                when(template.getForObject(openExchangeHistoricalUrl, OpenExchangeRates.class)).thenReturn(openExchangeExpectedRates);
        }

        @Test
        public void shouldReturnLiveCurrencyRates() {

                CurrencyRates result = service.getExchangeRates(source, currencies);
                assertEquals("USD", result.getSource());
                assertEquals(8, result.getRates().size());
        }

        @Test
        public void shouldReturnHistoricalCurrencyRates() {

                CurrencyRates result = service.getExchangeRates(source, currencies, LocalDate.of(2015, 10, 19));
                assertEquals("USD", result.getSource());
                assertEquals(8, result.getRates().size());
        }

}
