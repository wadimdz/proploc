package org.rakietowa.proploc.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.rakietowa.proploc.data.IPropContainer;
import org.rakietowa.proploc.data.IPropertyPersister;

public class ProplocImplTest {



	private ProplocImpl tested = null;

	@Mock
	private IPropertyPersister mockPersister;

	@Captor
	private ArgumentCaptor<HashMap<String, String>> mapCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		tested = new ProplocImpl(mockPersister);
	}

	@Test
	public void findUntranslatedTest() {
		final String baseName = "alala.properties";
		final String transName = "alala_pl.properties";
		final String prevDir = "prev";
		final String currDir = "curr";

		final String prevBasePath = prevDir + File.separator + baseName;
		final String prevTransPath = prevDir + File.separator + transName;
		final String currBasePath = currDir + File.separator + baseName;
		final String currTransPath = currDir + File.separator + transName;

		// previous
		mockPrevious(baseName, transName, prevDir, prevBasePath, prevTransPath);

		// current
		final Map<String, String> currBase = mockCurrent(baseName, transName, currDir, currBasePath, currTransPath);

		// when
		tested.findUntranslated(prevDir, currDir);
		
		// then
		ArgumentCaptor<String> fileName = ArgumentCaptor.forClass(String.class);
		Mockito.verify(mockPersister).savePropertyFile(fileName.capture(), mapCaptor.capture());
		assertEquals("UNTRANSLATED_alala_pl.properties", fileName.getValue());
		HashMap<String, String> savedMap = mapCaptor.getValue();
		assertEquals(3, savedMap.size());
		assertTrue(savedMap.containsKey("key3"));
		assertTrue(savedMap.containsKey("key2"));
		// changed
		assertEquals(currBase.get("key2"), savedMap.get("key2"));
		// new
		assertEquals(currBase.get("key3"), savedMap.get("key3"));
		// untranslated (base = translation)
		assertEquals(currBase.get("key5"), savedMap.get("key5"));
	}

	private Map<String, String> mockCurrent(final String baseName, final String transName, final String currDir,
			final String currBasePath, final String currTransPath) {
		Map<String, File> currMap = new HashMap<>();
		currMap.put(baseName, new File(currBasePath));
		currMap.put(transName, new File(currTransPath));

		final Map<String, String> currBase = new HashMap<>();
		currBase.put("key1", "currentValue1");
		currBase.put("key2", "currentValue2_Changed");
		currBase.put("key3", "currentValue3");
		currBase.put("key5", "currentValue5");

		final Map<String, String> currTrans = new HashMap<>();
		currTrans.put("key1", "currentValue1Trans");
		currTrans.put("key2", "currentValue2Trans");
		currTrans.put("key5", "currentValue5");

		IPropContainer currBaseCont = makePropContainerImpl(currBase);
		Mockito.when(mockPersister.readPropertyFile(currBasePath)).thenReturn(currBaseCont);
		IPropContainer currTransCont = makePropContainerImpl(currTrans);
		Mockito.when(mockPersister.readPropertyFile(currTransPath)).thenReturn(currTransCont);

		Mockito.when(mockPersister.listFilesInDir(new File(currDir))).thenReturn(currMap);
		return currBase;
	}

	private void mockPrevious(final String baseName, final String transName, final String prevDir,
			final String prevBasePath, final String prevTransPath) {
		Map<String, File> prevMap = new HashMap<>();
		prevMap.put(baseName, new File(prevBasePath));
		prevMap.put(transName, new File(prevTransPath));

		final Map<String, String> prevBase = new HashMap<>();
		prevBase.put("key0", "currentValue2");
		prevBase.put("key1", "currentValue1");
		prevBase.put("key2", "currentValue2");
		prevBase.put("key5", "currentValue5");

		final Map<String, String> prevTrans = new HashMap<>();
		prevTrans.put("key0", "currentValue1Trans");
		prevTrans.put("key1", "currentValue1Trans");
		prevTrans.put("key2", "currentValue2Trans");
		prevTrans.put("key5", "currentValue5");

		final IPropContainer prevBaseCont = makePropContainerImpl(prevBase);
		Mockito.when(mockPersister.readPropertyFile(prevBasePath)).thenReturn(prevBaseCont);
		final IPropContainer prevTransCont = makePropContainerImpl(prevTrans);
		Mockito.when(mockPersister.readPropertyFile(prevTransPath)).thenReturn(prevTransCont);
		
		Mockito.when(mockPersister.listFilesInDir(new File(prevDir))).thenReturn(prevMap);
	}

	private IPropContainer makePropContainerImpl(Map<String, String> aMap) {
		return new MockPropContainer(aMap);
	}
	
	private final class MockPropContainer implements IPropContainer {

		public MockPropContainer(Map<String, String> aMap) {
			fMap = new HashMap<>(aMap);
		}

		private Map<String, String> fMap;

		@Override
		public String getStringValue(String key) {
			return fMap.get(key);
		}

		@Override
		public List<String> getKeys() {
			return new ArrayList<String>(fMap.keySet());
		}
	}
}
