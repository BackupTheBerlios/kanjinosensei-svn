/**
 * @author Escallier Pierre
 * @file RefactoringInfos.java
 * @date 12 sept. 2008
 */
package epsofts.KanjiNoSensei;

import java.util.HashMap;
import java.util.Map;

import epsofts.KanjiNoSensei.metier.LearningProfile;

/**
 * 
 */
public class RefactoringInfos
{
	public static final Map<String, Package>	REFACTORED_PACKAGES	= new HashMap<String, Package>();

	static
	{
		REFACTORED_PACKAGES.put("metier", epsofts.KanjiNoSensei.metier.Dictionary.class.getPackage());
		REFACTORED_PACKAGES.put("utils", epsofts.KanjiNoSensei.utils.MyUtils.class.getPackage());
		REFACTORED_PACKAGES.put("vue", epsofts.KanjiNoSensei.vue.KanjiNoSensei.class.getPackage());
	}
}
