using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Principal;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.Filters;
using WebAPI.Models.Bases;
using WebAPI.Models.Exceptions;
using WebAPI.Models.Helpers;
using WebAPI.Models.ORM;

namespace WebAPI.Auth
{
    public class BasicAuthenticationAttribute : Attribute, IAuthenticationFilter
    {
        public string Realm { get; set; }

#pragma warning disable CS1998 // Async method lacks 'await' operators and will run synchronously
        public async Task AuthenticateAsync(HttpAuthenticationContext context, CancellationToken cancellationToken)
#pragma warning restore CS1998 // Async method lacks 'await' operators and will run synchronously
        {
            HttpRequestMessage request = context.Request;
            AuthenticationHeaderValue authorization = request.Headers.Authorization;
            if (context.ActionContext.ActionDescriptor.GetCustomAttributes<AllowAnonymousAttribute>().Any())
            {
                return;
            }

            if (authorization == null || authorization.Scheme != "Basic" || !request.Headers.Contains("Authorization-For"))
            {
                context.ErrorResult = new AuthenticationFailureResult(new ForbiddenExepction(), request);
                return;
            }

            try
            {
                var authFor = request.Headers.First(x => x.Key == "Authorization-For");
                if (authFor.Value.Contains("User"))
                {
                    var identity = new GenericIdentity(ExtractUserFromCredentials(authorization.Parameter).Username, "User");
                    context.Principal = new GenericPrincipal(identity, null);
                }
                else if (authFor.Value.Contains("Shop"))
                {
                    var identity = new GenericIdentity(ExtractShopFromCredentials(authorization.Parameter).Email, "Shop");
                    context.Principal = new GenericPrincipal(identity, null);
                }
                else
                {
                    context.ErrorResult = new AuthenticationFailureResult(new ForbiddenExepction(), request);
                    return;
                }
            }
            catch (Exception e)
            {
                context.ErrorResult = new AuthenticationFailureResult(e, request);
            }

        }

        public Task ChallengeAsync(HttpAuthenticationChallengeContext context, CancellationToken cancellationToken)
        {
            return Task.FromResult(0);
        }


        public virtual bool AllowMultiple
        {
            get { return false; }
        }

        private User ExtractUserFromCredentials(string credentials)
        {
            var encoding = Encoding.GetEncoding("iso-8859-1");
            credentials = encoding.GetString(Convert.FromBase64String(credentials));

            int separator = credentials.IndexOf(':');
            string name = credentials.Substring(0, separator);
            string password = credentials.Substring(separator + 1);
            string hashedPassword = Helper.Hash(password);

            User user = null;
            using (var db = new DbEntities())
            {

                if (name == "token")
                {
                    user = db.Tokens.Where(token => token.TokenValue == password).FirstOrDefault()?.User ?? throw new TokenNotFoundException();
                }
                else
                {
                    user = db.Users.Where(u => (u.Email == name || u.Username == name) && u.Password == hashedPassword).FirstOrDefault() ?? throw new InvalidCredentialsException();


                    if (user.Tokens != null && user.Tokens.Count == 1)
                    {
                        db.Tokens.RemoveRange(user.Tokens);
                    }

                    db.Tokens.Add(new Token
                    {
                        UserId = user.Id,
                        TokenValue = Guid.NewGuid().ToString()
                    });
                    db.SaveChanges();
                }
            }
            return user;

        }

        private Shop ExtractShopFromCredentials(string credentials)
        {
            var encoding = Encoding.GetEncoding("iso-8859-1");
            credentials = encoding.GetString(Convert.FromBase64String(credentials));

            int separator = credentials.IndexOf(':');
            string name = credentials.Substring(0, separator);
            string password = credentials.Substring(separator + 1);
            string hashedPassword = Helper.Hash(password);

            Shop shop = null;
            using (var db = new DbEntities())
            {

                if (name == "token")
                {
                    shop = db.Tokens.Where(token => token.TokenValue == password).FirstOrDefault()?.Shop ?? throw new TokenNotFoundException();
                }
                else
                {
                    shop = db.Shops.Where(u => u.Email == name && u.Password == hashedPassword).FirstOrDefault() ?? throw new InvalidCredentialsException();


                    if (shop.Tokens != null && shop.Tokens.Count == 1)
                    {
                        db.Tokens.RemoveRange(shop.Tokens);
                    }

                    db.Tokens.Add(new Token
                    {
                        ShopId = shop.Id,
                        TokenValue = Guid.NewGuid().ToString()
                    });
                    db.SaveChanges();
                }
            }
            return shop;

        }

    }
}