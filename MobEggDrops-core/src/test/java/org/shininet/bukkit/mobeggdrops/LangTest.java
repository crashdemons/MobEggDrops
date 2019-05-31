/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.mobeggdrops;

import com.github.crashdemons.mobeggdrops.InternalEggType;
import com.github.crashdemons.mobeggdrops.testutils.Mocks;
import com.github.crashdemons.mobeggdrops.testutils.TestOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import org.bukkit.Bukkit;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class LangTest {
    
    private final TestOutput out=new TestOutput(this);
    public LangTest() {
        Mocks.setupFakeServerSupport();
    }

    @Test
    public void testLangExistence() throws FileNotFoundException,IOException,URISyntaxException {
        out.println("testLangExistence");
        
        Properties props = new Properties();
        
        String path = new File( getClass().getProtectionDomain().getCodeSource().getLocation().toURI() ).getPath();
        out.println(path);
        FileInputStream resource = new FileInputStream(path+"/lang.properties");
        props.load(resource);
        
        for (InternalEggType skullType : InternalEggType.values()) {
            String value;
            value = props.getProperty("EGG_"+skullType.name());
            if(value==null)
                out.println("Egg display name property not found for: "+skullType.name());
                 //fail("Egg display name property not found for: "+skullType.name());
            value = props.getProperty("EGG_SPAWN_"+skullType.name());
            if(value==null)
                 fail("Egg Spawn-name property not found for: "+skullType.name());
        }
    }
    
}
