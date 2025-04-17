package com.encrypt.decrypt.kms.pii.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.beanutils.BeanUtils;

public abstract class PatchableObject {

	public PatchableObject updateValues(PatchableObject requestToUpdate, Map<String, String> valuesToUpdate) throws NoSuchElementException {
		valuesToUpdate.forEach((key, value) -> {
			try {
				BeanUtils.getProperty(requestToUpdate, key);
				BeanUtils.setProperty(requestToUpdate, key, value);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				throw new NoSuchElementException();
			}
		});
		return requestToUpdate;
	}

}
