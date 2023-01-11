// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.inject.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

/**
 * Unit test for the servlet utility class.
 *
 * @author ntang@google.com (Michael Tang)
 */
public class ServletUtilsTest extends TestCase {
  public void testGetContextRelativePath() {
    assertEquals(
        "/test.html", getContextRelativePath("/a_context_path", "/a_context_path/test.html"));
    assertEquals("/test.html", getContextRelativePath("", "/test.html"));
    assertEquals("/test.html", getContextRelativePath("", "/foo/../test.html"));
    assertEquals("/test.html", getContextRelativePath("", "/././foo/../test.html"));
    assertEquals("/test.html", getContextRelativePath("", "/foo/../../../../test.html"));
    assertEquals("/test.html", getContextRelativePath("", "/foo/%2E%2E/test.html"));
    // %2E == '.'
    assertEquals("/test.html", getContextRelativePath("", "/foo/%2E%2E/test.html"));
    // %2F == '/'
    assertEquals("/foo/%2F/test.html", getContextRelativePath("", "/foo/%2F/test.html"));
    // %66 == 'f'
    assertEquals("/foo.html", getContextRelativePath("", "/%66oo.html"));
  }

    public void testGetContextRelativePath_preserveQuery() {
        assertEquals("/foo?q=f", getContextRelativePath("", "/foo?q=f"));
        assertEquals("/foo?q=%20+%20", getContextRelativePath("", "/foo?q=%20+%20"));
    }

    public void testGetContextRelativePathWithWrongPath() {
        assertNull(getContextRelativePath("/a_context_path", "/test.html"));
    }

    public void testGetContextRelativePathWithRootPath() {
        assertEquals("/", getContextRelativePath("/a_context_path", "/a_context_path"));
    }

    public void testGetContextRelativePathWithEmptyPath() {
        assertNull(getContextRelativePath("", ""));
    }

    public void testNormalizedPath() {
        assertEquals("foobar", ServletUtils.normalizePath("foobar"));
        assertEquals("", ServletUtils.normalizePath("%2E"));
        assertEquals("", ServletUtils.normalizePath("%2E%2E"));
        assertEquals("foo+bar", ServletUtils.normalizePath("foo+bar"));
        assertEquals("foo%20bar", ServletUtils.normalizePath("foo bar"));
        assertEquals("foo%25-bar", ServletUtils.normalizePath("foo%-bar"));
        assertEquals("foo%25+bar", ServletUtils.normalizePath("foo%+bar"));
        assertEquals("foo%25-0bar", ServletUtils.normalizePath("foo%-0bar"));
        assertEquals("/bar", ServletUtils.normalizePath("/bar"));
        assertEquals("x/z", ServletUtils.normalizePath("x/%2E%2E/%2E%2E/y/%2E%2E/z"));
        assertEquals("bar", ServletUtils.normalizePath("%2E%2E/bar"));
        assertEquals("", ServletUtils.normalizePath(""));
        assertEquals("/bar", ServletUtils.normalizePath("/%2E%2E/bar"));
        assertEquals("", ServletUtils.normalizePath("/%2E%2E/bar/%2E%2E"));
        assertEquals("bar", ServletUtils.normalizePath("%2E%2E/bar/%2E%2E"));
        assertEquals("foo/bar", ServletUtils.normalizePath("foo/%2E%2E/bar"));
        assertEquals("foo/bar", ServletUtils.normalizePath("foo/%2E%2E/%2E%2E/bar"));
        assertEquals("foo/bar", ServletUtils.normalizePath("foo/%2E%2E/baz/%2E%2E/bar"));
        assertEquals("/bar", ServletUtils.normalizePath("/foo/%2E%2E/bar"));
        assertEquals("/bar", ServletUtils.normalizePath("/foo/%2E%2E/%2E%2E/bar"));
        assertEquals("foo/bar", ServletUtils.normalizePath("foo/%2E/bar"));
        assertEquals("foo/%2F/bar", ServletUtils.normalizePath("foo/%2F/bar"));
        assertEquals("%2F/bar", ServletUtils.normalizePath("%2F/bar"));
        assertEquals("bar", ServletUtils.normalizePath("%2E/bar"));
        assertEquals("/bar", ServletUtils.normalizePath("/%2E/bar"));
        assertEquals("///", ServletUtils.normalizePath("///"));
    }

    private String getContextRelativePath(String contextPath, String requestPath) {
        HttpServletRequest mock = mock(HttpServletRequest.class);
        when(mock.getContextPath()).thenReturn(contextPath);
        when(mock.getRequestURI()).thenReturn(requestPath);

        String contextRelativePath = ServletUtils.getContextRelativePath(mock);
        return contextRelativePath;
    }
}
