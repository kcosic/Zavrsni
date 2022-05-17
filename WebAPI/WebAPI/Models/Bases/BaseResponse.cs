using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.Responses
{
    public abstract class BaseResponse
    {
        protected readonly DateTime _timestamp;
        public DateTime Timestamp { get
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