package de.alpharogroup.compare.object;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.clone.object.CloneObjectExtensions;
import de.alpharogroup.test.objects.Gender;
import de.alpharogroup.test.objects.Person;
import lombok.experimental.ExtensionMethod;


@ExtensionMethod(CompareObjectExtensions.class)
public class CompareObjectExtensionsTest
{

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	public void setUp() throws Exception
	{
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test compare.
	 *
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	@Test(enabled = false)
	public void testCompare()
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		final Person sourceOjbect = new Person();
		sourceOjbect.setGender(Gender.MALE);
		sourceOjbect.setName("obelix");

		final Person objectToCompare = (Person)CloneObjectExtensions
			.cloneObjectQuietly(sourceOjbect);

		boolean result = CompareObjectExtensions.compare(sourceOjbect, objectToCompare);
		AssertJUnit.assertTrue("Cloned object should be equal with the source object.", result);

		objectToCompare.setGender(Gender.FEMALE);
		result = CompareObjectExtensions.compare(sourceOjbect, objectToCompare);
		AssertJUnit.assertFalse(
			"Object to compare should be not equal with the source object because it has changed.",
			result);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(enabled = true)
	public void testCompareTo()
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		final List<Person> persons = new ArrayList<>();
		final Person obelix = new Person();
		obelix.setGender(Gender.MALE);
		obelix.setName("obelix");

		final Person asterix = new Person();
		asterix.setGender(Gender.MALE);
		asterix.setName("asterix");

		final Person miraculix = new Person();
		miraculix.setGender(Gender.MALE);
		miraculix.setName("miraculix");

		final int i = CompareObjectExtensions.compareTo(asterix, obelix, "name");

		System.out.println(i);

		persons.add(obelix);
		persons.add(asterix);
		persons.add(miraculix);
		System.out.println("Unsorted Persons:");
		System.out.println(persons.toString());
		final Comparator defaultComparator = new BeanComparator("name");
		Collections.sort(persons, defaultComparator);
		System.out.println("Sorted Persons by name:");
		System.out.println(persons.toString());
		Collections.reverse(persons);
		System.out.println("Sorted Persons by name reversed:");
		System.out.println(persons.toString());
	}

}

