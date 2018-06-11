package ca.uqac.lif.texlint;

import static org.junit.Assert.*;

import org.junit.Test;

import static ca.uqac.lif.texlint.AnnotatedString.CRLF;
import static ca.uqac.lif.texlint.AnnotatedString.CRLF_SIZE;

public class AnnotatedStringTest 
{
	@Test
	public void testLength1()
	{
		AnnotatedString as = new AnnotatedString();
		as.append("Hello world!");
		assertEquals(12, as.length());
	}
	
	@Test
	public void testLength2()
	{
		AnnotatedString as = new AnnotatedString();
		as.append("Hello").appendNewLine().append("world!");
		assertEquals(11 + CRLF_SIZE, as.length());
	}
	
	@Test
	public void testAppend1()
	{
		AnnotatedString as = new AnnotatedString();
		as.append("Hello ", Range.make(0, 0, 5));
		as.append("world!", Range.make(1, 10, 15));
		assertEquals("Hello world!", as.toString());
		Position p = as.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as.getSourcePosition(new Position(0, 7));
		assertNotNull(p);
		assertEquals(1, p.getLine());
		assertEquals(11, p.getColumn());
		p = as.getSourcePosition(new Position(1, 7));
		assertNull(p);
	}
	
	@Test
	public void testAppend2()
	{
		AnnotatedString as = new AnnotatedString();
		as.append("Hello", Range.make(0, 0, 4));
		as.appendNewLine();
		as.append("world!", Range.make(1, 10, 15));
		assertEquals("Hello" + CRLF + "world!", as.toString());
		Position p = as.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as.getSourcePosition(new Position(0, 7));
		assertNull(p); // There is no 7th column on line 0
		p = as.getSourcePosition(new Position(1, 1));
		assertNotNull(p);
		assertEquals(1, p.getLine());
		assertEquals(11, p.getColumn());
		p = as.getSourcePosition(new Position(1, 7));
		assertNull(p);
	}
	
	@Test
	public void testAppend3()
	{
		AnnotatedString as = new AnnotatedString();
		as.append("Hello", Range.make(0, 0, 4));
		as.append(" ");
		as.append("world!", Range.make(1, 10, 15));
		assertEquals("Hello world!", as.toString());
		Position p = as.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as.getSourcePosition(new Position(0, 5));
		assertNull(p); // No association registered for the space between hello and world
		p = as.getSourcePosition(new Position(0, 7));
		assertNotNull(p);
		assertEquals(1, p.getLine());
		assertEquals(11, p.getColumn());
		p = as.getSourcePosition(new Position(1, 7));
		assertNull(p);
	}
	
	@Test
	public void testAppend4()
	{
		AnnotatedString as_orig = new AnnotatedString();
		as_orig.append("Hello", Range.make(0, 0, 4));
		as_orig.append(" ");
		as_orig.append("world!", Range.make(1, 10, 15));
		AnnotatedString as = new AnnotatedString();
		as.append(as_orig);
		assertEquals("Hello world!", as.toString());
		Position p = as.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as.getSourcePosition(new Position(0, 5));
		assertNull(p); // No association registered for the space between hello and world
		p = as.getSourcePosition(new Position(0, 7));
		assertNotNull(p);
		assertEquals(1, p.getLine());
		assertEquals(11, p.getColumn());
		p = as.getSourcePosition(new Position(1, 7));
		assertNull(p);
	}
	
	@Test
	public void testAppend5()
	{
		AnnotatedString as_orig = new AnnotatedString();
		as_orig.append("Hello", Range.make(0, 0, 4));
		as_orig.append(" ");
		as_orig.append("world!", Range.make(1, 10, 15));
		AnnotatedString as = new AnnotatedString();
		as.append("Foo bar", Range.make(10, 4, 10));
		as.append(as_orig);
		assertEquals("Foo barHello world!", as.toString());
		Position p = as.getSourcePosition(new Position(0, 1));
		assertEquals(10, p.getLine());
		assertEquals(5, p.getColumn());
		p = as.getSourcePosition(new Position(0, 8));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as.getSourcePosition(new Position(0, 12));
		assertNull(p); // No association registered for the space between hello and world
		p = as.getSourcePosition(new Position(0, 14));
		assertNotNull(p);
		assertEquals(1, p.getLine());
		assertEquals(11, p.getColumn());
		p = as.getSourcePosition(new Position(1, 7));
		assertNull(p);
	}
	
	@Test
	public void testAppend6()
	{
		AnnotatedString as_orig = new AnnotatedString();
		as_orig.append("Hello", Range.make(0, 0, 4));
		as_orig.appendNewLine();
		as_orig.append("world!", Range.make(1, 10, 15));
		AnnotatedString as = new AnnotatedString();
		as.append("Foo bar", Range.make(10, 4, 10));
		as.append(as_orig);
		assertEquals("Foo barHello" + CRLF + "world!", as.toString());
		Position p = as.getSourcePosition(new Position(0, 1));
		assertEquals(10, p.getLine());
		assertEquals(5, p.getColumn());
		p = as.getSourcePosition(new Position(0, 8));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as.getSourcePosition(new Position(1, 0));
		assertNotNull(p);
		assertEquals(1, p.getLine());
		assertEquals(10, p.getColumn());
		p = as.getSourcePosition(new Position(1, 7));
		assertNull(p);
	}
	
	@Test
	public void testSubstring1()
	{
		AnnotatedString as_orig = new AnnotatedString();
		as_orig.append("Hello world!", Range.make(0, 0, 11));
		AnnotatedString as_sub = as_orig.substring(Range.make(0, 0, 3));
		assertEquals("Hell", as_sub.toString());
		Position p = as_sub.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as_sub.getSourcePosition(new Position(0, 4));
		assertNull(p);
	}
	
	@Test
	public void testSubstring2()
	{
		AnnotatedString as_orig = new AnnotatedString();
		as_orig.append("Hello", Range.make(0, 0, 4)).appendNewLine().append("world!", Range.make(0, 6, 11));
		AnnotatedString as_sub = as_orig.substring(Range.make(0, 0, 1, 2));
		assertEquals("Hello" + CRLF + "wor", as_sub.toString());
		Position p = as_sub.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(1, p.getColumn());
		p = as_sub.getSourcePosition(new Position(1, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(7, p.getColumn());
	}
	
	@Test
	public void testSubstring3()
	{
		AnnotatedString as_orig = new AnnotatedString();
		as_orig.append("Hello", Range.make(0, 0, 4)).appendNewLine().append("world!", Range.make(0, 6, 11));
		AnnotatedString as_sub = as_orig.substring(Range.make(0, 2, 1, 2));
		assertEquals("llo" + CRLF + "wor", as_sub.toString());
		Position p = as_sub.getSourcePosition(new Position(0, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(3, p.getColumn());
		p = as_sub.getSourcePosition(new Position(1, 1));
		assertNotNull(p);
		assertEquals(0, p.getLine());
		assertEquals(7, p.getColumn());
	}
}