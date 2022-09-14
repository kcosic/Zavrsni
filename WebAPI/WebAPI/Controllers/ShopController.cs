using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class ShopController : BaseController
    {
        public ShopController() : base(nameof(ShopController)) { }

        [HttpGet]
        [Route("api/Shop/{id}")]
        public BaseResponse RetrieveShop(int id, bool expanded = false)
        {
            try
            {
                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(shop.ToDTO(!expanded));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Shop")]
        public BaseResponse RetrieveShops()
        {
            try
            {
                var shops = Db.Shops.Where(x => !x.Deleted).ToList();
                if (shops == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Shop.ToListDTO(shops));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Shop/{id}/Childs")]
        public BaseResponse RetrieveChildShops(int id)
        {
            try
            {
                var parentShop = Db.Shops.Find(id);
                if (parentShop == null || parentShop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var childShops = parentShop.ChildShops.Where(x => !x.Deleted).ToList();
                if (childShops == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Shop.ToListDTO(childShops));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Shop/{id}/Parent")]
        public BaseResponse RetrieveParentShop(int id)
        {
            try
            {
                var childShop = Db.Shops.Find(id);
                if (childShop == null || childShop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var parentShop = childShop.ParentShop;
                if (parentShop == null || parentShop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(parentShop);
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Shop/{id}/Reviews")]
        public BaseResponse RetrieveShopReviews(int id)
        {
            try
            {
                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var shopReviews = shop.Reviews.Where(x=> !x.Deleted).ToList();
                if (shopReviews == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(shopReviews);
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/Shop/{id}")]
        public BaseResponse DeleteShop(int id)
        {
            try
            {
                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                shop.DateDeleted = DateTime.Now;
                shop.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }


        [HttpPut]
        [Route("api/Shop/{id}")]
        public BaseResponse UpdateShop([FromUri] int id, [FromBody] ShopDTO shopDTO)
        {
            try
            {
                if (id == null || shopDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                if (shop.Email != shopDTO.Email && Db.Shops.Where(x => x.Email == shopDTO.Email).Count() > 0)
                {
                    throw new Exception("Email is taken");
                }
                if (shop.LegalName != shopDTO.LegalName && Db.Shops.Where(x => x.LegalName == shopDTO.LegalName).Count() > 0)
                {
                    throw new Exception("Legal name is taken");
                }
                if (shop.ShortName != shopDTO.ShortName && Db.Shops.Where(x => x.ShortName == shopDTO.ShortName).Count() > 0)
                {
                    throw new Exception("Short name is taken");
                }
                shop.DateModified = DateTime.Now;
                shop.Email = shopDTO.Email;
                shop.LegalName = shopDTO.LegalName;
                shop.LocationId = shopDTO.LocationId;
                shop.ParentShopId = shopDTO.ParentShopId;
                shop.Vat = shopDTO.Vat;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}
