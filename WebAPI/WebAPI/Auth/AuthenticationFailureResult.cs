using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using WebAPI.Models.Bases;
using WebAPI.Models.Enums;
using WebAPI.Models.Responses;

namespace WebAPI.Auth
{
    public class AuthenticationFailureResult : IHttpActionResult
    {

        public HttpRequestMessage Request { get; private set; }

        public Exception Ex { get; set; }

        public AuthenticationFailureResult(Exception exception, HttpRequestMessage request)
        {
            Request = request;
            Ex = exception;
        }

        public Task<HttpResponseMessage> ExecuteAsync(CancellationToken cancellationToken)
        {
            return Task.FromResult(Execute());
        }

        private HttpResponseMessage Execute()
        {
            HttpResponseMessage response = new HttpResponseMessage(HttpStatusCode.Unauthorized)
            {
                RequestMessage = Request,
                Content = JsonContent.Create(
                    new ErrorResponse
                    {
                        Message = Ex.Message,
                        ErrorCode = Ex.GetType().BaseType == typeof(BaseException) ? (Ex as BaseException).ErrorCode : ErrorCodeEnum.UnexpectedError,
                        Status = HttpStatusCode.Unauthorized,
                        IsSuccess = false
                    },
                    null,
                    new JsonSerializerOptions
                    {
                        WriteIndented = true,
                        IncludeFields = true
                    })
            };
            return response;
        }
    }
}