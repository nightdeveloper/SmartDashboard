package com.github.nightdeveloper.smartdashboard;

import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import com.github.nightdeveloper.smartdashboard.property.CamerasProperty;
import com.github.nightdeveloper.smartdashboard.service.ArchiveService;
import com.github.nightdeveloper.smartdashboard.service.FileService;
import com.github.nightdeveloper.smartdashboard.service.UrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ActiveProfiles(profiles = Profiles.TEST)
@ExtendWith(MockitoExtension.class)
public class ArchiveServiceTest {

    @MockBean
    private UrlService urlService;

    @MockBean
    private FileService fileService;

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private CamerasProperty camerasProperty;

    @Test
    public void testArchiveSuccess() throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Mockito.when(fileService.getFileOutputStream(any())).thenReturn(outputStream);

        archiveService.getCamerasAndMoveToArchive();

        Mockito.verify(urlService, Mockito.times(camerasProperty.getList().size()))
                .copyCameraImageToStream(any(), eq(outputStream));
    }

}
