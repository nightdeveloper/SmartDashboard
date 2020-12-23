package com.github.nightdeveloper.smartdashboard;

import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import com.github.nightdeveloper.smartdashboard.property.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = Profiles.TEST)
public class PropertiesTest {

    private static final Logger logger = LogManager.getLogger(PropertiesTest.class);

    @Autowired
    private UsersProperty usersProperty;

    @Autowired
    private CamerasProperty camerasProperty;

    @Autowired
    private DevicesProperty devicesProperty;

    @Test
    void testUsersProperty() {
        Assertions.assertNotNull(usersProperty.getAllowed());
        Assertions.assertTrue(usersProperty.getAllowed().size() > 0);

        for(UserProperty userProperty : usersProperty.getAllowed()) {
            Assertions.assertNotNull(userProperty.getLogin());
        }
    }

    @Test
    void testCamerasProperty() {

        ArchiveProperty archive = camerasProperty.getArchive();
        Assertions.assertNotNull(archive);
        Assertions.assertNotNull(archive.getPath());
        Assertions.assertNotNull(archive.getCron());

        Assertions.assertNotNull(camerasProperty.getList());

        for(CameraProperty cameraProperty : camerasProperty.getList()) {
            Assertions.assertNotNull(cameraProperty.getName());
            Assertions.assertNotNull(cameraProperty.getUrl());
            Assertions.assertNotNull(cameraProperty.getCurrent());
        }
    }

    @Test
    void testDevicesProperty() {
        Assertions.assertNotNull(devicesProperty.getLocation());
        Assertions.assertNotNull(devicesProperty.getList());

        for(DeviceProperty deviceProperty : devicesProperty.getList()) {
            Assertions.assertNotNull(deviceProperty.getId());
            Assertions.assertNotNull(deviceProperty.getLocation());
            Assertions.assertNotNull(deviceProperty.getName());
            Assertions.assertNotNull(deviceProperty.getClassName());
        }
    }
}
