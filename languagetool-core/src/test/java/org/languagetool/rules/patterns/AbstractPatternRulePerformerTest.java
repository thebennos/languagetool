/* LanguageTool, a natural language style checker
 * Copyright (C) 2013 Daniel Naber (http://www.danielnaber.de)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.rules.patterns;

import org.junit.Test;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.chunking.ChunkTag;
import org.languagetool.language.Demo;

import java.util.Collections;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class AbstractPatternRulePerformerTest {

  @Test
  public void testTestAllReadings() throws Exception {
    Element element1 = new Element("foo", false, false, false);
    PatternRule simpleRule = new PatternRule("FAKE", new Demo(), Collections.singletonList(element1), "descr", "message", "short");
    ElementMatcher elemMatcher = new ElementMatcher(element1);

    AbstractPatternRulePerformer p = new MockAbstractPatternRulePerformer(simpleRule, null);

    assertTrue(p.testAllReadings(tokenReadings("foo", null), elemMatcher, null, 0, 0, 0));
    assertFalse(p.testAllReadings(tokenReadings("bar", null), elemMatcher, null, 0, 0, 0));
    assertTrue(p.testAllReadings(tokenReadings("foo", "myChunk"), elemMatcher, null, 0, 0, 0));
    assertTrue(p.testAllReadings(tokenReadings("foo", "otherChunk"), elemMatcher, null, 0, 0, 0));
  }

  @Test
  public void testTestAllReadingsWithChunks() throws Exception {
    Element chunkElement = new Element(null, false, false, false);
    chunkElement.setChunkElement(new ChunkTag("myChunk"));
    PatternRule simpleRule = new PatternRule("FAKE", new Demo(), Collections.singletonList(chunkElement), "descr", "message", "short");
    ElementMatcher elemMatcher = new ElementMatcher(chunkElement);

    AbstractPatternRulePerformer p = new MockAbstractPatternRulePerformer(simpleRule, null);

    assertFalse(p.testAllReadings(tokenReadings("bar", null), elemMatcher, null, 0, 0, 0));
    assertTrue(p.testAllReadings(tokenReadings("bar", "myChunk"), elemMatcher, null, 0, 0, 0));
    assertFalse(p.testAllReadings(tokenReadings("bar", "otherChunk"), elemMatcher, null, 0, 0, 0));
  }

  private AnalyzedTokenReadings[] tokenReadings(String token, String chunkTag) {
    AnalyzedTokenReadings tokenReadings1 = new AnalyzedTokenReadings(new AnalyzedToken(token, "pos", "lemma"), 0);
    if (chunkTag != null) {
      tokenReadings1.setChunkTags(Collections.singletonList(new ChunkTag(chunkTag)));
    }
    return new AnalyzedTokenReadings[] { tokenReadings1 };
  }

  class MockAbstractPatternRulePerformer extends AbstractPatternRulePerformer {
    protected MockAbstractPatternRulePerformer(AbstractPatternRule rule, Unifier unifier) {
      super(rule, unifier);
    }
  }
}
