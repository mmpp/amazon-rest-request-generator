package org.mmpp.amazon.rest.request.generator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.mmpp.amazon.rest.request.model.AbstractRequest;
import org.mmpp.amazon.rest.request.model.ItemLookupRequest;
import org.mmpp.amazon.rest.request.model.ItemSearchRequest;
import org.mmpp.amazon.rest.request.model.Request;
import org.mmpp.amazon.rest.request.model.SearchType;

public class URLGenerator {
	private static final String UTF8_CHARSET = "UTF-8";
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	private javax.crypto.Mac _mac = null;


	private javax.crypto.Mac getMac(){
		if(_mac==null){
			try {
				_mac = javax.crypto.Mac.getInstance(HMAC_SHA256_ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(HMAC_SHA256_ALGORITHM + " is unsupported!", e);
			}

		}
		return _mac;
	}
	public String parse(Request request) throws RequestParameterException {
		throwRequestParameterException(request);
		String secretKey = request.getItemRequest().getAccessCertificate().getSecretKey();
		String endpoint = request.getEndpoint();

		java.util.SortedMap<String,String> params = parseParams(request); 

		SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
		String canonicalQS = canonicalize(sortedParamMap);
		String toSign =
			request.REQUEST_METHOD + "\n"
			+ endpoint + "\n"
			+ request.REQUEST_URI + "\n"
			+ canonicalQS;

		String hmac = hmac(toSign,secretKey);
		String sig = percentEncodeRfc3986(hmac);
		String url = "http://" + endpoint + request.REQUEST_URI + "?" +
							canonicalQS + "&Signature=" + sig;

		return url;
	}

	private void throwRequestParameterException(Request request) throws RequestParameterException {
		if(request==null)
			throw new RequestParameterException("Requestが指定されていません");
		AbstractRequest itemRequest = request.getItemRequest();
		if(itemRequest==null)
			throw new RequestParameterException("Request:(ItemRequestを確認してください)");
		throwAbstractRequestParameterException(itemRequest);
		
		if(itemRequest instanceof ItemLookupRequest){
			ItemLookupRequest itemLookupRequest = (ItemLookupRequest)itemRequest;
			throwItemLookupRequestParameterException(itemLookupRequest);
		}else if(itemRequest instanceof ItemSearchRequest){
			ItemSearchRequest itemSearchRequest = (ItemSearchRequest)itemRequest;
			throwItemSearchRequestParameterException(itemSearchRequest);
		}else{
			throw new RequestParameterException("ItemRequestが不明です");
		}
	}
	private void throwAbstractRequestParameterException(AbstractRequest itemRequest) throws RequestParameterException {
		java.util.List<String> warnParams = new java.util.LinkedList<String>();
		if(itemRequest.getAccessCertificate()==null){
			warnParams.add("アクセス証明書");
		}else{
			if(itemRequest.getAccessCertificate().getSecretKey()==null)
				warnParams.add("SecretKey");
			if(itemRequest.getAccessCertificate().getAccessKey()==null)
				warnParams.add("AssociateTag");
		}
		if(itemRequest.getAffiliateAccount()==null){
			warnParams.add("AssociateTag");
		}else if(itemRequest.getAffiliateAccount().getTrackingID()==null)
			warnParams.add("AssociateTag");

		if(warnParams.size()==0)
			return ;
		RequestParameterException exception = createRequestParameterException("ItemSearchRequest",warnParams);
		throw exception;

	}
	private SortedMap<String, String> parseParams(Request request) throws RequestParameterException {
		SortedMap<String, String> params = new TreeMap<String, String> ();
		params.put("Service","AWSECommerceService");

		AbstractRequest itemRequest = request.getItemRequest();
		if(itemRequest instanceof ItemLookupRequest){
			ItemLookupRequest itemLookupRequest = (ItemLookupRequest)itemRequest;
			
			params.put("Operation","ItemLookup");
			params.put("IdType",itemLookupRequest.getIdType().toString());
			params.put("ItemId",itemLookupRequest.getItemId());
			if(itemLookupRequest.getSearchIndex()!=null)
				params.put("SearchIndex",itemLookupRequest.getSearchIndex().toString());
		}else if(itemRequest instanceof ItemSearchRequest){
			ItemSearchRequest itemSearchRequest = (ItemSearchRequest)itemRequest;
			
			params.put("Operation","ItemSearch");
			for(SearchType searchType : itemSearchRequest.getSearchParameters().keySet()){
				params.put(searchType.toString(), itemSearchRequest.getSearchParameters().get(searchType));
			}
			params.put("SearchIndex",itemSearchRequest.getSearchIndex().toString());
			if(itemSearchRequest.getItemPage()!=null)
				params.put("ItemPage",String.valueOf(itemSearchRequest.getItemPage()));
			//params.put("__mk_ja_JP","カタカナ");
		}
		String awsAccessKeyId = request.getItemRequest().getAccessCertificate().getAccessKey();
		String timestamp = formatTimestamp(request.getItemRequest().getTimestamp());
		String associateTag = request.getItemRequest().getAffiliateAccount().getTrackingID();

		params.put("AssociateTag",associateTag);

		params.put("AWSAccessKeyId", awsAccessKeyId);
		params.put("Timestamp", timestamp);
		return params;
	}
	private void throwItemLookupRequestParameterException(ItemLookupRequest itemLookupRequest) throws RequestParameterException {
		java.util.List<String> warnParams = new java.util.LinkedList<String>();
		if(itemLookupRequest.getIdType()==null)
			warnParams.add("IdType");
		if(itemLookupRequest.getItemId()==null)
			warnParams.add("ItemId");
		if(warnParams.size()==0)
			return ;
		RequestParameterException exception = createRequestParameterException("ItemLookupRequest",warnParams);
		throw exception;
	}
	/**
	 * Seach
	 * @param itemSearchRequest
	 * @throws RequestParameterException 
	 */
	private void throwItemSearchRequestParameterException(ItemSearchRequest itemSearchRequest) throws RequestParameterException {
		java.util.List<String> warnParams = new java.util.LinkedList<String>();
		if(itemSearchRequest.getSearchParameters().size()==0)
			warnParams.add("SearchType(Title等)");
		if(itemSearchRequest.getSearchIndex()==null)
			warnParams.add("SearchIndex");
		if(warnParams.size()==0)
			return ;
		RequestParameterException exception = createRequestParameterException("ItemSearchRequest",warnParams);
		throw exception;
	}
	/**
	 * RequestParameterExceptionを作成します
	 * @param requestName ItemRequest名
	 * @param params 違反パラメタ
	 * @return RequestParameterException
	 */
	private RequestParameterException createRequestParameterException(String requestName,List<String> params) {
		StringBuffer buf = new StringBuffer();
		for(String param  : params ){
			buf.append(",");
			buf.append(param);
		}
		String parameter = buf.substring(1);
		
		return new RequestParameterException(requestName+":("+parameter+"を確認してください)");
	}
	private String hmac(String stringToSign,String secretKey) {
		String signature = null;
		byte[] data;
		byte[] rawHmac;
		try {
			data = stringToSign.getBytes(UTF8_CHARSET);
			
			byte[] secretyKeyBytes = secretKey.getBytes(UTF8_CHARSET);
			javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
			getMac().init(secretKeySpec);

			rawHmac = getMac().doFinal(data);
			Base64 encoder = new Base64();
			signature = new String(encoder.encode(rawHmac));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(HMAC_SHA256_ALGORITHM + " is unsupported!", e);
		}
		return signature;
	}

	/**
	 * 現在時刻文字列を取得します
	 * @return 現在時刻文字列
	 */
	private static String formatTimestamp(java.util.Date date) {
		String timestamp = null;
		if(date==null)
			date = new java.util.Date();
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
		timestamp = dfm.format(date);
		return timestamp;
	}

	/**
	 * マップを文字列に変換します
	 * @param map マップクラス
	 * @return 文字列
	 */
	public static String canonicalize(SortedMap<String, String> map){
		if (map.isEmpty()) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, String> kvpair:map.entrySet()) {
			buffer.append("&");
			buffer.append(percentEncodeRfc3986(kvpair.getKey()));
			buffer.append("=");
			buffer.append(percentEncodeRfc3986(kvpair.getValue()));
		}
		return buffer.toString().substring(1);
	}

	/**
	 * URLをアクセス可能なエンコードします
	 * @param url 変換前URL
	 * @return エスケープ後URL
	 */
	public static String percentEncodeRfc3986(String url) {
		try {
			return java.net.URLEncoder.encode(url, UTF8_CHARSET)
				.replace("+", "%20")
				.replace("*", "%2A")
				.replace("%7E", "~");
		} catch (UnsupportedEncodingException e) {
		}
		return url;
	}

}
