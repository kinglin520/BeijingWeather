///*
// * Copyright (C) 2015 Square, Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.superman.beijingweather.httpserver.retrofit;
//
//import com.google.gson.Gson;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Type;
//
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Converter;
//
//public final class TGsonConverterFactory extends Converter.Factory {
//  /**
//   * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
//   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
//   */
//  public static TGsonConverterFactory create() {
//    return create(new Gson());
//  }
//
//  /**
//   * Create an instance using {@code gson} for conversion. Encoding to JSON and
//   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
//   */
//  public static TGsonConverterFactory create(Gson gson) {
//    return new TGsonConverterFactory(gson);
//  }
//
//  private final Gson gson;
//
//  private TGsonConverterFactory(Gson gson) {
//    if (gson == null) throw new NullPointerException("gson == null");
//    this.gson = gson;
//  }
//
//  @Override
//  public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
//    return new TGsonResponseBodyConverter<>(gson, type);
//  }
//
//  @Override
//  public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
//    return new GsonRequestBodyConverter<>(gson, type);
//  }
//}
