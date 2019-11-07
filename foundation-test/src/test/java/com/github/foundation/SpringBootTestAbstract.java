package com.github.foundation;

import com.github.foundation.test.FoundationServer;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoundationServer.class)
@ActiveProfiles("unittest")
public abstract class SpringBootTestAbstract {
}
