package de.alpharogroup.compare.object;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import de.alpharogroup.comparators.ComparatorExtensions;
import lombok.experimental.UtilityClass;

/**
 * The class {@link CompareObjectExtensions} provide methods for compare an object with another
 * given object.
 */
@UtilityClass
public final class CompareObjectExtensions
{

	/** The logger constant. */
	private static final Logger LOG = Logger.getLogger(CompareObjectExtensions.class.getName());

	/**
	 * Compares the given two objects.
	 *
	 * @param sourceOjbect
	 *            the source ojbect
	 * @param objectToCompare
	 *            the object to compare
	 * @return true, if successful otherwise false
	 * @throws IllegalAccessException
	 *             Thrown if this {@code Method} object is enforcing Java language access control
	 *             and the underlying method is inaccessible.
	 * @throws InvocationTargetException
	 *             Thrown if the property accessor method throws an exception
	 * @throws NoSuchMethodException
	 *             Thrown if a matching method is not found or if the name is "&lt;init&gt;"or
	 *             "&lt;clinit&gt;".
	 */
	@SuppressWarnings("rawtypes")
	public static boolean compare(final Object sourceOjbect, final Object objectToCompare)
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if (sourceOjbect == null || objectToCompare == null
			|| !sourceOjbect.getClass().equals(objectToCompare.getClass()))
		{
			throw new IllegalArgumentException("Object should not be null and be the same type.");
		}
		final Map beanDescription = BeanUtils.describe(sourceOjbect);
		beanDescription.remove("class");
		final Map clonedBeanDescription = BeanUtils.describe(objectToCompare);
		clonedBeanDescription.remove("class");
		for (final Object key : beanDescription.keySet())
		{
			if (compareTo(sourceOjbect, objectToCompare, key.toString()) != 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Compares the given two objects and returns the result as {@link int}.
	 *
	 * @param sourceOjbect
	 *            the source object
	 * @param objectToCompare
	 *            the object to compare
	 * @return the resulted int value
	 * @throws IllegalAccessException
	 *             Thrown if this {@code Method} object is enforcing Java language access control
	 *             and the underlying method is inaccessible.
	 * @throws InvocationTargetException
	 *             Thrown if the property accessor method throws an exception
	 * @throws NoSuchMethodException
	 *             Thrown if a matching method is not found or if the name is "&lt;init&gt;"or
	 *             "&lt;clinit&gt;".
	 */
	@SuppressWarnings("rawtypes")
	public static int compareTo(final Object sourceOjbect, final Object objectToCompare)
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if (sourceOjbect == null || objectToCompare == null
			|| !sourceOjbect.getClass().equals(objectToCompare.getClass()))
		{
			throw new IllegalArgumentException("Object should not be null and be the same type.");
		}
		final Map beanDescription = BeanUtils.describe(sourceOjbect);
		beanDescription.remove("class");
		final Map clonedBeanDescription = BeanUtils.describe(objectToCompare);
		clonedBeanDescription.remove("class");
		int result = 0;
		for (final Object key : beanDescription.keySet())
		{
			result = compareTo(sourceOjbect, objectToCompare, key.toString());
			if (result == 0)
			{
				continue;
			}
		}
		return result;
	}

	/**
	 * Compares the given object over the given property.
	 *
	 * @param sourceOjbect
	 *            the source ojbect
	 * @param objectToCompare
	 *            the object to compare
	 * @param property
	 *            the property
	 * @return the resulted int value
	 * @throws IllegalAccessException
	 *             Thrown if this {@code Method} object is enforcing Java language access control
	 *             and the underlying method is inaccessible.
	 * @throws InvocationTargetException
	 *             Thrown if the property accessor method throws an exception
	 * @throws NoSuchMethodException
	 *             Thrown if a matching method is not found or if the name is "&lt;init&gt;"or
	 *             "&lt;clinit&gt;".
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compareTo(final Object sourceOjbect, final Object objectToCompare,
		final String property)
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		final Integer nullCheck = ComparatorExtensions.nullCheck(
			BeanUtils.describe(sourceOjbect).get(property),
			BeanUtils.describe(objectToCompare).get(property));
		if (nullCheck != null)
		{
			return nullCheck;
		}
		return new BeanComparator(property).compare(sourceOjbect, objectToCompare);
	}

	/**
	 * Compares the given object over the given property quietly.
	 *
	 * @param sourceOjbect
	 *            the source ojbect
	 * @param objectToCompare
	 *            the object to compare
	 * @param property
	 *            the property
	 * @return the int
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compareToQuietly(final Object sourceOjbect, final Object objectToCompare,
		final String property)
	{
		Map<?, ?> beanDescription = null;
		try
		{
			beanDescription = BeanUtils.describe(sourceOjbect);
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("BeanUtils.describe(sourceOjbect) throws an exception...", e);
			return 0;
		}
		Map<?, ?> clonedBeanDescription = null;
		try
		{
			clonedBeanDescription = BeanUtils.describe(objectToCompare);
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("BeanUtils.describe(objectToCompare) throws an exception...", e);
			return 0;
		}
		final Object sourceAttribute = beanDescription.get(property);
		final Object changedAttribute = clonedBeanDescription.get(property);
		if (sourceAttribute == null && changedAttribute == null)
		{
			return 0;
		}
		if (sourceAttribute != null && changedAttribute == null)
		{
			return 1;
		}
		else if (sourceAttribute == null && changedAttribute != null)
		{
			return -1;
		}
		return new BeanComparator(property).compare(sourceOjbect, objectToCompare);
	}


	/**
	 * Gets the compare to result.
	 *
	 * @param sourceOjbect
	 *            the source ojbect
	 * @param objectToCompare
	 *            the object to compare
	 * @return the compare to result
	 * @throws IllegalAccessException
	 *             Thrown if this {@code Method} object is enforcing Java language access control
	 *             and the underlying method is inaccessible.
	 * @throws InvocationTargetException
	 *             Thrown if the property accessor method throws an exception
	 * @throws NoSuchMethodException
	 *             Thrown if a matching method is not found or if the name is "&lt;init&gt;"or
	 *             "&lt;clinit&gt;".
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Integer> getCompareToResult(final Object sourceOjbect,
		final Object objectToCompare)
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if (sourceOjbect == null || objectToCompare == null
			|| !sourceOjbect.getClass().equals(objectToCompare.getClass()))
		{
			throw new IllegalArgumentException("Object should not be null and be the same type.");
		}
		final Map beanDescription = BeanUtils.describe(sourceOjbect);
		beanDescription.remove("class");
		final Map clonedBeanDescription = BeanUtils.describe(objectToCompare);
		clonedBeanDescription.remove("class");
		final Map<String, Integer> compareResult = new HashMap<>();
		for (final Object key : beanDescription.keySet())
		{
			compareResult.put(key.toString(),
				Integer.valueOf(compareTo(sourceOjbect, objectToCompare, key.toString())));
		}
		return compareResult;
	}

}
