using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using WebAPI.Auth;
using WebAPI.Models.Enums;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Models
{
    [BasicAuthentication]
    public abstract class BaseController : ApiController
    {
        internal readonly string _controllerName;
        internal readonly DbEntities _db;
        internal User _user;

        public User AuthUser
        {
            get
            {
                if (_user != null)
                {
                    return _user;
                }

                if (_user == null && User?.Identity?.Name != null)
                {
                    _user = _db.Users.Where(u => u.Username == User.Identity.Name).FirstOrDefault();
                    return _user;
                }

                throw null;
            }
        }

        public DbEntities Db 
        { 
            get 
            {
                return _db; 
            } 
        }

        public BaseController(string controllerName)
        {
            _controllerName = controllerName;
            _db = new DbEntities();
        }

        #region Responses

        public ListResponse<T> CreateOkResponse<T>(List<T> data)
        {
            return new ListResponse<T>
            {
                Data = data,
                Message = "Success!",
                Status = System.Net.HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        public SingleResponse<T> CreateOkResponse<T>(T data)
        {
            return new SingleResponse<T>
            {
                Data = data,
                Message = "Success!",
                Status = System.Net.HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        public PaginatedResponse<T> CreateOkResponse<T>(List<T> data, int page, int pageSize, int total)
        {
            return new PaginatedResponse<T>
            {
                Data = data,
                Page = page,
                PageSize = pageSize,
                Total = total,
                Message = "Success!",
                Status = System.Net.HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        public ErrorResponse CreateErrorResponse(Exception e, ErrorCodeEnum errorCode)
        {
            return CreateErrorResponse(e.Message, errorCode);
        }

        public ErrorResponse CreateErrorResponse(string message, ErrorCodeEnum errorCode)
        {
            Log(SeverityEnum.Error, message);

            return new ErrorResponse
            {
                Message = "message",
                Status = System.Net.HttpStatusCode.InternalServerError,
                ErrorCode = errorCode,
                IsSuccess = false
            };
        }

        #endregion

        #region Log
        private void Log(SeverityEnum severity, string message)
        {
            Log(severity, message, DateTime.Now);

        }
        private void Log(SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(ApplicationEnum.API, severity, message, timestamp);

        }

        private void Log(ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(AuthUser.Id, application, severity, message, timestamp);

        }

        private void Log(int userId, ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(userId, application, severity, _controllerName, message, timestamp);

        }

        private void Log(int userId, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(userId, ApplicationEnum.API, severity, _controllerName, message, timestamp);
        }

        private void Log(int userId, ApplicationEnum application, SeverityEnum severity, string source, string message, DateTime timestamp)
        {
            Db.Logs.Add(new Log
            {
                UserId = userId,
                Application = Enum.GetName(typeof(ApplicationEnum), application),
                Severity = Enum.GetName(typeof(SeverityEnum), severity),
                Source = source.Length > 200 ? source.Substring(0, 199) : source,
                Message = message,
                Timestamp = timestamp
            });
            Db.SaveChanges();
        }
        #endregion
    }
}