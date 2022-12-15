using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web;

namespace WebAPI.Models.Responses
{
    public class BaseResponse
    {
        public string Message { get; set; }
        public HttpStatusCode Status { get; set; }

        public bool IsSuccess { get; set; }


        internal readonly DateTime _timestamp;
        public DateTime Timestamp
        {
            get
            {
                return _timestamp;
            }
        }

        public BaseResponse()
        {
            _timestamp = DateTime.Now;
        }
    }
}