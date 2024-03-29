package org.mmpp.amazon.rest.request.generator;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

import org.mmpp.amazon.rest.request.generator.RequestParameterException;
import org.mmpp.amazon.rest.request.generator.URLGenerator;
import org.mmpp.amazon.rest.request.model.AbstractRequest;
import org.mmpp.amazon.rest.request.model.AccessCertificate;
import org.mmpp.amazon.rest.request.model.AffiliateAccount;
import org.mmpp.amazon.rest.request.model.IdType;
import org.mmpp.amazon.rest.request.model.ItemLookupRequest;
import org.mmpp.amazon.rest.request.model.ItemSearchRequest;
import org.mmpp.amazon.rest.request.model.Request;
import org.mmpp.amazon.rest.request.model.SearchIndex;
import org.mmpp.amazon.rest.request.model.SearchType;

public class URLGeneratorTest {

	@Test
	public void testItemLookupRequestISBNBook() throws ParseException, RequestParameterException{
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";
		String itemId = "9784091831941";
		java.util.Date timestamp = getDateFormat("yyyy/MM/dd HH:mm:ss").parse("2012/07/22 15:33:00");
		Request request = new Request();
		
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemLookupRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemLookupRequest.setAffiliateAccount(affiateAccount);
		
		itemLookupRequest.setIdType(IdType.ISBN);
		itemLookupRequest.setItemId(itemId);
		itemLookupRequest.setSearchIndex(SearchIndex.Books);

		itemLookupRequest.setTimestamp(timestamp);
		request.setItemRequest(itemLookupRequest);
		String url = new URLGenerator().parse(request);
		
		assertEquals("http://ecs.amazonaws.jp/onca/xml?AWSAccessKeyId=ACCESSKEY&AssociateTag=TRACKINGID&IdType=ISBN&ItemId=9784091831941&Operation=ItemLookup&SearchIndex=Books&Service=AWSECommerceService&Timestamp=2012-07-22T15%3A33%3A00Z&Signature=NIYH10zgeT3waN3Lud8Xtc1LaCoCvrnb03cDbI8AHcM%3D",url);
		
	}

	@Test
	public void testItemSearchRequestASIN() throws ParseException, RequestParameterException{
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";
		String itemId = "B005030N36";
		java.util.Date timestamp = getDateFormat("yyyy/MM/dd HH:mm:ss").parse("2012/07/22 15:33:00");
		Request request = new Request();
		
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemLookupRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemLookupRequest.setAffiliateAccount(affiateAccount);
		
		itemLookupRequest.setIdType(IdType.ASIN);
		itemLookupRequest.setItemId(itemId);

		itemLookupRequest.setTimestamp(timestamp);
		request.setItemRequest(itemLookupRequest);
		String url = new URLGenerator().parse(request);
		
		assertEquals("http://ecs.amazonaws.jp/onca/xml?AWSAccessKeyId=ACCESSKEY&AssociateTag=TRACKINGID&IdType=ASIN&ItemId=B005030N36&Operation=ItemLookup&Service=AWSECommerceService&Timestamp=2012-07-22T15%3A33%3A00Z&Signature=O7WODQEvp3LXxmXqw6xPqXedGU24LLiZxqz4Rll%2BE2g%3D",url);
		
	}
	@Test
	public void testItemSearchRequestAuthor() throws ParseException, RequestParameterException{
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";
		java.util.Date timestamp = getDateFormat("yyyy/MM/dd HH:mm:ss").parse("2012/07/22 15:33:00");

		String author = "神塚 ときお";
		
		Request request = new Request();
		
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemSearchRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemSearchRequest.setAffiliateAccount(affiateAccount);

		itemSearchRequest.setSearchIndex(SearchIndex.Books);
		itemSearchRequest.getSearchParameters().put(SearchType.Author,author);
		itemSearchRequest.setTimestamp(timestamp);

		request.setItemRequest(itemSearchRequest);
		String url = new URLGenerator().parse(request);
		
		assertEquals("http://ecs.amazonaws.jp/onca/xml?AWSAccessKeyId=ACCESSKEY&AssociateTag=TRACKINGID&Author=%E7%A5%9E%E5%A1%9A%20%E3%81%A8%E3%81%8D%E3%81%8A&Operation=ItemSearch&SearchIndex=Books&Service=AWSECommerceService&Timestamp=2012-07-22T15%3A33%3A00Z&Signature=4riYzUw1KDIOI%2BkE6OPrchJQNmfSMT%2B8xRakAbCqgiA%3D",url);
		
	}
	@Test
	public void testItemSearchRequestAuthorPage2() throws ParseException, RequestParameterException{
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";
		java.util.Date timestamp = getDateFormat("yyyy/MM/dd HH:mm:ss").parse("2012/07/22 15:33:00");

		String author = "神塚 ときお";
		
		Request request = new Request();
		
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemSearchRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemSearchRequest.setAffiliateAccount(affiateAccount);

		itemSearchRequest.setSearchIndex(SearchIndex.Books);
		itemSearchRequest.getSearchParameters().put(SearchType.Author,author);
		itemSearchRequest.setItemPage(2);
		itemSearchRequest.setTimestamp(timestamp);

		request.setItemRequest(itemSearchRequest);
		String url = new URLGenerator().parse(request);
		
		assertEquals("http://ecs.amazonaws.jp/onca/xml?AWSAccessKeyId=ACCESSKEY&AssociateTag=TRACKINGID&Author=%E7%A5%9E%E5%A1%9A%20%E3%81%A8%E3%81%8D%E3%81%8A&ItemPage=2&Operation=ItemSearch&SearchIndex=Books&Service=AWSECommerceService&Timestamp=2012-07-22T15%3A33%3A00Z&Signature=TFTJndItNMCkL9B9%2Fz6WkDfxOTDa2ZBiWl0VClLqWIE%3D",url);
		
	}
	@Test
	public void testItemSearchRequestAuthorSearchIndexException() throws ParseException{
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";

		String author = "神塚 ときお";
		
		Request request = new Request();
		
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemSearchRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemSearchRequest.setAffiliateAccount(affiateAccount);

//		itemSearchRequest.setSearchIndex(SearchIndex.Books);
		itemSearchRequest.getSearchParameters().put(SearchType.Author,author);
		
		request.setItemRequest(itemSearchRequest);
		try {
			new URLGenerator().parse(request);
			fail();
		} catch (RequestParameterException e) {
			assertEquals("ItemSearchRequest:(SearchIndexを確認してください)",e.getMessage());
		}
		
	}

	@Test
	public void testRequestParameterException() throws ParseException {
		Request request = new Request();
		 try {
			new URLGenerator().parse(request);
		} catch (RequestParameterException e) {
			assertEquals("Request:(ItemRequestを確認してください)",e.getMessage());
		}
	}
	@Test
	public void testRequestParameterExceptionNull() throws ParseException {
		 try {
			new URLGenerator().parse(null);
		} catch (RequestParameterException e) {
			assertEquals("Requestが指定されていません",e.getMessage());
		}
	}

	@Test
	public void testNullAuthAbstractRequestParameterException() throws ParseException {
		Request request = new Request();
		AbstractRequest itemRequest = new AbstractRequest(){};
		request.setItemRequest(itemRequest);

		 try {
			new URLGenerator().parse(request);
		} catch (RequestParameterException e) {
			assertEquals("ItemSearchRequest:(アクセス証明書,AssociateTagを確認してください)",e.getMessage());
		}
	}

	@Test
	public void testAuthEmptyAbstractRequestParameterException() throws ParseException {
		Request request = new Request();
		AbstractRequest itemRequest = new AbstractRequest(){};
		AccessCertificate accessCertificate = new AccessCertificate();
		itemRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		itemRequest.setAffiliateAccount(affiateAccount);
		request.setItemRequest(itemRequest);
		 try {
			new URLGenerator().parse(request);
		} catch (RequestParameterException e) {
			assertEquals("ItemSearchRequest:(SecretKey,AssociateTag,AssociateTagを確認してください)",e.getMessage());
		}
	}
	@Test
	public void testAbstractRequestParameterException() throws ParseException {
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";
		Request request = new Request();
		AbstractRequest itemRequest = new AbstractRequest(){};
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemRequest.setAffiliateAccount(affiateAccount);
		request.setItemRequest(itemRequest);
		 try {
			new URLGenerator().parse(request);
		} catch (RequestParameterException e) {
			assertEquals("ItemRequestが不明です",e.getMessage());
		}
	}
	@Test
	public void testNullItemSearchRequestParameterException() throws ParseException {
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";

		Request request = new Request();
		
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemSearchRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemSearchRequest.setAffiliateAccount(affiateAccount);
		
		request.setItemRequest(itemSearchRequest);
		try {
			new URLGenerator().parse(request);
			fail();
		} catch (RequestParameterException e) {
			assertEquals("ItemSearchRequest:(SearchType(Title等),SearchIndexを確認してください)",e.getMessage());
		}
	}
	@Test
	public void testNullItemLookupRequestParameterException() throws ParseException {
		String accessKey = "ACCESSKEY";
		String key = "SECRETKEY";
		String trackingid = "TRACKINGID";
		java.util.Date timestamp = getDateFormat("yyyy/MM/dd HH:mm:ss").parse("2012/07/22 15:33:00");
		Request request = new Request();
		
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(accessKey);
		accessCertificate.setSecretKey(key);
		itemLookupRequest.setAccessCertificate(accessCertificate);
		AffiliateAccount affiateAccount = new AffiliateAccount();
		affiateAccount.setTrackingID(trackingid);
		itemLookupRequest.setAffiliateAccount(affiateAccount);
		
		itemLookupRequest.setTimestamp(timestamp);
		request.setItemRequest(itemLookupRequest);
		 try {
			new URLGenerator().parse(request);
		} catch (RequestParameterException e) {
			assertEquals("ItemLookupRequest:(IdType,ItemIdを確認してください)",e.getMessage());
		}
	}
	@Test
	public void testTimestamp() throws ParseException{
		String datevalue = "2012/07/22 15:33:00";
		DateFormat dfm = getDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date timestamp = dfm.parse(datevalue);
		assertEquals(datevalue,dfm.format(timestamp));
	}
	public DateFormat getDateFormat(String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat;
	}
}
