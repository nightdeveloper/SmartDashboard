package com.github.nightdeveloper.smartdashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.repository.SensorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TelemetryControllerTest {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected SensorRepository mockSensorRepository;

    private String testTelemetryController(String request) throws Exception {

        String result = mockMvc.perform(post(Constants.REST_TELEMETRY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(content().contentType(Constants.JSON_UTF8))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        verify(mockSensorRepository, times(1)).save(any());

        return result;
    }

    @Test
    public void testTelemetryControllerSwitch1() throws Exception {
        String switch1 = "{\"deviceId\":\"0x00158d0002c93b1c\",\"battery\":100,\"voltage\":3055," +
                "\"linkquality\":136,\"click\":\"single\"}";

        Assertions.assertEquals(Constants.RESPONSE_OK, testTelemetryController(switch1));
    }

    @Test
    public void testTelemetryControllerSwitch2() throws Exception {
        String switch2 = "{\"deviceId\":\"0x00158d0002c93b48\",\"battery\":100,\"voltage\":3055," +
                "\"linkquality\":136,\"click\":\"single\"}";

        Assertions.assertEquals(Constants.RESPONSE_OK, testTelemetryController(switch2));
    }

    @Test
    public void testTelemetryControllerPlug() throws Exception {
        String plug = "{\"deviceId\":\"0x00158d00046585b9\",\"state\":\"OFF\",\"power\":0," +
                "\"voltage\":null,\"consumption\":0,\"temperature\":25,\"linkquality\":78}";

        Assertions.assertEquals(Constants.RESPONSE_OK, testTelemetryController(plug));
    }

    @Test
    public void testTelemetryControllerComfortOutdoor() throws Exception {
        String comfortOutdoor = "{\"deviceId\":\"0x00158d00046585b9\",\"battery\":51,\"voltage\":2915," +
                "\"temperature\":14.72,\"humidity\":51.65,\"pressure\":994,\"linkquality\":65}";

        Assertions.assertEquals(Constants.RESPONSE_OK, testTelemetryController(comfortOutdoor));
    }

    @Test
    public void testTelemetryControllerComfortCabinet() throws Exception {
        String comfortCabinet = "{\"deviceId\":\"0x00158d0003f0fb54\",\"battery\":51,\"voltage\":2915," +
                "\"temperature\":14.72,\"humidity\":51.65,\"pressure\":994,\"linkquality\":65}";

        Assertions.assertEquals(Constants.RESPONSE_OK, testTelemetryController(comfortCabinet));
    }

    @Test
    public void testTelemetryControllerComfortBedroom() throws Exception {
        String comfortBedroom = "{\"deviceId\":\"0x00158d0003f0fdcf\",\"battery\":51,\"voltage\":2915," +
                "\"temperature\":14.72,\"humidity\":51.65,\"pressure\":994,\"linkquality\":65}";

        Assertions.assertEquals(Constants.RESPONSE_OK, testTelemetryController(comfortBedroom));
    }
}
